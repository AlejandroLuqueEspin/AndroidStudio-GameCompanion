package com.example.myapplicationaaa1.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplicationaaa1.R
import com.example.myapplicationaaa1.adapters.StreamsAdapter
import com.example.myapplicationaaa1.model.*
import com.example.myapplicationaaa1.network.RickAndMortyHttpClient
import com.example.myapplicationaaa1.network.TwitchHttpClient
import kotlinx.android.synthetic.main.fragment_twitch.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class Streams_Fragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater!!.inflate(R.layout.fragment_twitch, container, false)

        // Inflate the layout for this fragment
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBarTwitch.visibility = VISIBLE


        getStreamsTwitch()

        streamsSearchButton.setOnClickListener {
            progressBarTwitch.visibility = VISIBLE

            getStreamsTwitch()
        }

    }


    private fun getRickAndMortyCharacters() {
        RickAndMortyHttpClient.service.getCharacters().enqueue(
            object : Callback<CharacterResponse> {
                override fun onResponse(
                    call: Call<CharacterResponse>,
                    response: Response<CharacterResponse>
                ) {
                    Log.i("StreamsFragment", "")
                    if (response.code() == 200) {//response.isSuccessful
                        Log.i("StreamsFragment", "TODO OK")
                        Log.i(
                            "StreamsFragment",
                            response.body()?.results?.toString() ?: "Empty Body"
                        )
                    } else {
                        Log.w(
                            "StreamsFragment",
                            response.errorBody()?.string() ?: "Null error body"
                        )
                    }
                }

                override fun onFailure(call: Call<CharacterResponse>, t: Throwable) {
                    Log.w("StreamsFragment", "Null error body")

                }

            })
    }



    private fun getStreamsTwitch() {
        TwitchHttpClient.service.getStreams().enqueue(object : Callback<TWStreamsResponse> {
            override fun onResponse(
                call: Call<TWStreamsResponse>,
                response: Response<TWStreamsResponse>
            ) {
                response.body()?.data?.let { streams ->
                    for (stream in streams) {
                        Log.i(
                            "MainActivity",
                            "Title: ${stream.title} and image: ${stream.imageUrl} and username: ${stream.username} and user_id: ${stream.user_id}and game_id: ${stream.game_id}"
                        )
                        Log.i(
                            "MainActivity",
                            "Stream Url: https://www.twitch.tv/${stream.username}"
                        )
                    }
                    if(progressBarTwitch!=null)
                    progressBarTwitch.visibility = GONE


                    // Configure Recyclerview
                    if(recyclerViewTwitch!=null){
                    recyclerViewTwitch.adapter = StreamsAdapter(ArrayList(streams.orEmpty()),activity!!.supportFragmentManager,requireContext())
                    recyclerViewTwitch.layoutManager = LinearLayoutManager(context)
//                    configureOnClickRecyclerView()
                    }
                }
            }

            override fun onFailure(call: Call<TWStreamsResponse>, t: Throwable) {
                t.printStackTrace()
            }

        })

    }
//    private fun configureOnClickRecyclerView() {
//
//        val adapter: StreamsAdapter = recyclerViewTwitch.adapter as StreamsAdapter
//        val nullableIndexLayout: Int? = R.layout.item_stream as Int? // allowed, always works
//
//        if (nullableIndexLayout != null) {
//            ItemClickSupport.addTo(recyclerViewTwitch, nullableIndexLayout)
//                .setOnItemClickListener(object : ItemClickSupport.OnItemClickListener {
//                    override fun onItemClicked(recyclerViewTwitch: RecyclerView, position: Int, v: View) {
//                        // 1 - Get user from adapter
//                        val stream = adapter.GetStreams(position)
//                        // 2 - Show result in a Toast
//                        Toast.makeText(
//                            context,
//                            "You clicked on user : " + stream.username,
//                            Toast.LENGTH_SHORT
//                        ).show()
//
//                        //open detail activity
////                        val intent = Intent(requireContext(), PostDetailActivity::class.java)
////                        intent.putExtra("postUserUID", news.userUID) //User id
////                        intent.putExtra("postUID", news.postUID) //Post id
////                        startActivity(intent)
//
//                    }
//                })
//        }
//
//    }

}