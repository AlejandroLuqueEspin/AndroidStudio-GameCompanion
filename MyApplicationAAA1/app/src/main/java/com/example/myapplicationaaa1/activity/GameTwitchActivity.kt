package com.example.myapplicationaaa1.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.myapplicationaaa1.R
import kotlinx.android.synthetic.main.activity_game_twitch.*

class GameTwitchActivity : AppCompatActivity() {

    var name:String?=null
    var box_art_url:String?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val b = getIntent().getExtras()
        if (b != null) {
            name=b.getString("twitchGameName") //User id
            box_art_url=b.getString("twitchGameUrl") //User id

        } else
            finish()


        setContentView(R.layout.activity_game_twitch)
        UpdateUI()

    }

    fun UpdateUI(){
        gameNameTwitch.text= name
        Glide.with(imageGameTwitch.context).load(box_art_url).into(imageGameTwitch)
    }
}
