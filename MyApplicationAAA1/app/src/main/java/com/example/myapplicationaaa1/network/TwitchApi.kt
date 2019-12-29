package com.example.myapplicationaaa1.network

import com.example.myapplicationaaa1.model.TWGameResponse
import com.example.myapplicationaaa1.model.TWStreamsResponse
import com.example.myapplicationaaa1.model.TWUserModel
import com.example.myapplicationaaa1.model.TWUserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface TwitchApi{

    @Headers("Client-ID: ywvglt0gib8rqdly0ejobehqfi071m")
    @GET("streams")
    fun getStreams(): Call<TWStreamsResponse>


    @Headers("Client-ID: ywvglt0gib8rqdly0ejobehqfi071m")
    @GET("games")
    fun getGames(@Query("id") gameId: String): Call<TWGameResponse>

    @Headers("Client-ID: ywvglt0gib8rqdly0ejobehqfi071m")
    @GET("users")
    fun getUser(@Query("id") gameId: String): Call<TWUserResponse>

}