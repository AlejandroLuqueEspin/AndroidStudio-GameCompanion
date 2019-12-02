package com.example.myapplicationaaa1.network

import retrofit2.Retrofit

class RickAndMortyHttpClient {
    companion object{
        //Http Client
        private val retrofit=Retrofit.Builder()
            .baseUrl("http://rickandmortyapi.com/api/")
            .build()

        //Rick And Morty Service
        val service= retrofit.create<RickAndMortyApi>(RickAndMortyApi::class.java)
    }
}