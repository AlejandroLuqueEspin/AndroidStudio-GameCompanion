package com.example.myapplicationaaa1.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplicationaaa1.R
import com.example.myapplicationaaa1.activity.GameTwitchActivity
import com.example.myapplicationaaa1.activity.UserTwitchActivity
import com.example.myapplicationaaa1.model.TWStreamsModel
import com.example.myapplicationaaa1.dialogs.GameDialog
import com.example.myapplicationaaa1.model.TWGameResponse
import com.example.myapplicationaaa1.model.TWUserResponse
import com.example.myapplicationaaa1.network.TwitchHttpClient
import kotlinx.android.synthetic.main.item_stream.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class StreamsAdapter(var listOfStreams: ArrayList<TWStreamsModel>,_fragmentmANAGER: FragmentManager,_context: Context) : RecyclerView.Adapter<StreamsAdapter.ViewHolder>() {

    val fragmentManager=_fragmentmANAGER

    val context:Context=_context

    class ViewHolder(item: View) : RecyclerView.ViewHolder(item) {

        val username: TextView = item.userNameViewTwitch
        val title: TextView = item.titleViewTwitch
        val imageUserView: ImageView = item.imageTwitch

        val playerButton:Button=item.playerSearchButton
        val gameButton:Button=item.gameSearchButton

        val imageGame:ImageView=item.imageGameTwitch
    }


    override fun getItemCount(): Int {
        return listOfStreams.count()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val item: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_stream, parent, false)
        return ViewHolder(item)
    }



    // Update Items
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val stream = listOfStreams[position]

        holder.username.text = stream.username
        holder.title.text=stream.title

        Glide.with(holder.imageUserView.context).load(listOfStreams[position].imageUrl)
            .into(holder.imageUserView)


        holder.gameButton.setOnClickListener {
            //open detail activity
            listOfStreams[position].game_id?.let { it1 -> openGameDialog(it1) }
        }
        holder.playerButton.setOnClickListener {
            listOfStreams[position].user_id?.let { it1 -> openUserDialog(it1) }
        }
    }
    fun openGameDialog(game_uid:String){

        TwitchHttpClient.service.getGames(game_uid).enqueue(object :
            Callback<TWGameResponse> {
            override fun onResponse(
                call: Call<TWGameResponse>,
                response: Response<TWGameResponse>
            ) {
                response.body()?.data?.let { games ->
                    for (game in games) {
                        Log.w("CLICKPOSTDELETE", "CLICKPOSTDELETE")

                        val intent = Intent(context, GameTwitchActivity::class.java)
                        intent.putExtra("twitchGameName", game.name) //User id
                        intent.putExtra("twitchGameUrl", game.imageUrl) //User id
                        startActivity(context,intent,null)                   }
                }
            }

            override fun onFailure(call: Call<TWGameResponse>, t: Throwable) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        })






    }
    fun openUserDialog(user_id:String){

        TwitchHttpClient.service.getUser(user_id).enqueue(object : Callback<TWUserResponse> {
            override fun onResponse(
                call: Call<TWUserResponse>,
                response: Response<TWUserResponse>
            ) {
                response.body()?.data?.let { users ->
                    for (user in users) {
                        Log.w("CLICKPOSTDELETE", "CLICKPOSTDELETE")

                        val intent = Intent(context, UserTwitchActivity::class.java)
                        intent.putExtra("twitchDescription", user.description) //User id
                        intent.putExtra("twitchDisplayName", user.display_name) //User id
                        intent.putExtra("twitchEmail", user.email) //User id
                        intent.putExtra("twitchUserImageUrl", user.imageUrl) //User id
                        intent.putExtra("twitchLogin", user.login) //User id
                        startActivity(context,intent,null)

                  }
                }
            }

            override fun onFailure(call: Call<TWUserResponse>, t: Throwable) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        })

    }
    fun GetStreams(position: Int): TWStreamsModel {
        return listOfStreams.get(position)
    }


}