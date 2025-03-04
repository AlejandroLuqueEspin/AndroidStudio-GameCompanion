package com.example.myapplicationaaa1.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RickAndMortyHttpClient {
    companion object{
        //Http Client
        private val retrofit=Retrofit.Builder()
            .baseUrl("https://rickandmortyapi.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        //Rick And Morty Service
        val service= retrofit.create<RickAndMortyApi>(RickAndMortyApi::class.java)
    }
}