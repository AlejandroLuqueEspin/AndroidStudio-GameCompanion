package com.example.myapplicationaaa1.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.myapplicationaaa1.R
import kotlinx.android.synthetic.main.activity_user_twitch.*

class UserTwitchActivity : AppCompatActivity() {

    var twitchDescription:String?=null
    var twitchDisplayName:String?=null
    var twitchEmail:String?=null
    var twitchUserImageUrl:String?=null
    var twitchLogin:String?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val b = getIntent().getExtras()
        if (b != null) {
            twitchDescription=b.getString("twitchDescription") //User id
            twitchDisplayName=b.getString("twitchDisplayName") //User id
            twitchEmail= b.getString("twitchEmail")
            twitchUserImageUrl=b.getString("twitchUserImageUrl") //User id
            twitchLogin=b.getString("twitchLogin") //User id

        } else
            finish()


        setContentView(R.layout.activity_user_twitch)
        UpdateUI()

    }

    fun UpdateUI(){
        loginTwitch.text= "Login Nam: e"+twitchLogin
        emailTwitch.text= "Email: "+twitchEmail
        displayNameTwitch.text= "DisplayName: "+twitchDisplayName
        descriptionTwitch.text= "Description: "+twitchDescription
        Glide.with(imageProfileTwitch.context).load(twitchUserImageUrl).into(imageProfileTwitch)
    }
}
