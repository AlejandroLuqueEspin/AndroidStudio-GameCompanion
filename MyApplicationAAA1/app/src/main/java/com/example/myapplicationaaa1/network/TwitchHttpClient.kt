package com.example.myapplicationaaa1.network
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TwitchHttpClient {
    companion object {
        private var retrofit = Retrofit.Builder()
            .baseUrl("https://api.twitch.tv/helix/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create<TwitchApi>(TwitchApi::class.java)
    }

}