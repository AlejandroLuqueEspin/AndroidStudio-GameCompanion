package com.example.myapplicationaaa1.network

import com.example.myapplicationaaa1.model.StreamsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query

interface TwitchApi{

    @Headers("Client-ID: ywvglt0gib8rqdly0ejobehqfi071m")
    @GET ("streams")
    fun getStreams(@Query("game_id")gameId:String?=null): Call<StreamsResponse>


}