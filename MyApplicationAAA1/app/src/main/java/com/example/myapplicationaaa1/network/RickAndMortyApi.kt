package com.example.myapplicationaaa1.network

import com.example.myapplicationaaa1.model.CharacterResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET

interface RickAndMortyApi {

    @GET("character")
    fun getCharacters():Call<CharacterResponse>
}