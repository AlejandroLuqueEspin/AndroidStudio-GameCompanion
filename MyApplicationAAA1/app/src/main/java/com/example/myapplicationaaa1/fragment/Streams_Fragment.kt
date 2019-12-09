package com.example.myapplicationaaa1.fragment

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.myapplicationaaa1.R
import com.example.myapplicationaaa1.model.CharacterResponse
import com.example.myapplicationaaa1.model.StreamsResponse
import com.example.myapplicationaaa1.network.RickAndMortyHttpClient
import com.example.myapplicationaaa1.network.TwitchHttpClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class Streams_Fragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater!!.inflate(R.layout.fragment_test_twitch, container, false)

        // Inflate the layout for this fragment
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getStreams()
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
                    Log.i("StreamsFragment", response.body()?.results?.toString() ?: "Empty Body")
                } else {
                    Log.w("StreamsFragment", response.errorBody()?.string() ?: "Null error body")
                }
            }

            override fun onFailure(call: Call<CharacterResponse>, t: Throwable) {
                Log.w("StreamsFragment", "Null error body")

            }

        })
}

private fun getStreams() {

    TwitchHttpClient.service.getStreams(gameId = "33214").enqueue(object : Callback<StreamsResponse> {
        override fun onFailure(call: Call<StreamsResponse>, t: Throwable) {
            Log.w("StreamsFragtment",t.localizedMessage)

        }
        override fun onResponse(call: Call<StreamsResponse>, response: Response<StreamsResponse>) {

            if(response.isSuccessful){
                val streams=response.body()?.results?:emptyList()
                Log.i("StreamsFragtment",streams.toString())
            }else{
                Log.w("StreamsFragtment",response.errorBody()?.string()?:"null error body")
            }
        }

    })
}
