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
import com.example.myapplicationaaa1.network.RickAndMortyHttpClient
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

        RickAndMortyHttpClient.service.getCharacters().enqueue(
            object : Callback<ResponseBody>{
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>)
                {
                Log.i("StreamsFragment","")
                    if(response.code()==200){//response.isSuccessful
                        Log.i("StreamsFragment","TODO OK")
                        Log.i("StreamsFragment",response.body()?.string()?:"Empty Body")
                    }
                    else{
                        Log.w("StreamsFragment",response.errorBody()?.string()?:"Null error body")
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.w("StreamsFragment","Null error body")

                }

            })

        }

    }

