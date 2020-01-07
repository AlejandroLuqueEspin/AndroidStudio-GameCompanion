package com.example.myapplicationaaa1.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplicationaaa1.R
import com.example.myapplicationaaa1.model.NewsModel
import com.example.myapplicationaaa1.model.UserModel
import com.example.myapplicationaaa1.dialogs.DeletePostDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.item_news.view.*
import androidx.fragment.app.FragmentManager


class NewsAdapter(var listOfNews: ArrayList<NewsModel>, _fragmentmANAGER: FragmentManager) :
    RecyclerView.Adapter<NewsAdapter.ViewHolder>() {
    val fragmentManager = _fragmentmANAGER

    class ViewHolder(item: View) : RecyclerView.ViewHolder(item) {

        val username: TextView = item.userNameView
        val textPosted: TextView = item.textPosted
        val imageUserView: ImageView = item.userImageView
        val eraseButton: ImageButton = item.erasePostButton
    }

    override fun getItemCount(): Int {
        return listOfNews.count()
    }

    // Create item_joke View
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val item: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_news, parent, false)
        return ViewHolder(item)
    }


    // Update Items
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val news = listOfNews[position]

        holder.username.text = news.userName
        holder.textPosted.text = news.textPosted
        // 1 - Download image from URL
        // 2 - Cache Image
        // 3 - Load image into ImageView
        listOfNews[position].userUID?.let { checkedUserUID ->
            FirebaseFirestore.getInstance()
                .collection("Users")
                .document(checkedUserUID)
                .get()
                .addOnSuccessListener { documentSnapshot ->

                    val user = documentSnapshot.toObject(UserModel::class.java)
                    if (user != null) {
                        Glide.with(holder.imageUserView.context).load(user.url)
                            .into(holder.imageUserView)
                    }
                }.addOnFailureListener { e ->

                }

        }
        Glide.with(holder.imageUserView.context).load(listOfNews[position])
            .into(holder.imageUserView)

        if (FirebaseAuth.getInstance().currentUser?.uid ?: String() != listOfNews[position].userUID) {
            holder.eraseButton.visibility = GONE
        } else {
            holder.eraseButton.visibility = VISIBLE
            holder.eraseButton.setOnClickListener {
                listOfNews[position].postUID?.let { it1 ->
                        openDialog(it1,  listOfNews[position].upPostUID)

                }
            }
        }
    }


    fun openDialog(_postUID: String, _UPpostUID: String?) {

        Log.w("CLICKPOSTDELETE", "CLICKPOSTDELETE")
        if (_UPpostUID==null) {
            val exampleDialog = DeletePostDialog(_postUID, _UPpostUID, false)
            exampleDialog.show(fragmentManager, "example dialog")

        } else {
            val exampleDialog = DeletePostDialog(_postUID, _UPpostUID, true)
            exampleDialog.show(fragmentManager, "example dialog")

        }

    }

    fun GetNews(position: Int): NewsModel {
        return listOfNews.get(position)
    }


}