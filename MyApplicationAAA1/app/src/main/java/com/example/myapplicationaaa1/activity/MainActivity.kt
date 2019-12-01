package com.example.myapplicationaaa1.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplicationaaa1.R
import com.example.myapplicationaaa1.fragment.Login_Fragment
import com.example.myapplicationaaa1.fragment.News_Fragment
import com.google.firebase.analytics.FirebaseAnalytics
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FirebaseAnalytics.getInstance(this).logEvent("AppOpen", null)

        //Para inicar el gragmentlayout con algo al inicio
        val profileFragment = Login_Fragment()
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.add(fragmentContainer.id, profileFragment)
        fragmentTransaction.commit()


        bottomNavView.setOnNavigationItemSelectedListener { menuItem ->
            //            val parametres =Bundle()
//                parametres.putString("tab","chat")

            when (menuItem.itemId) {
                R.id.chat -> {
                    FirebaseAnalytics.getInstance(this).logEvent("CHAT_TabClicked", null)

                }
                R.id.home -> {
                    FirebaseAnalytics.getInstance(this).logEvent("HOME_TabClicked", null)

                    val newsFragment = News_Fragment()
                    val fragmentTransactionNews = supportFragmentManager.beginTransaction()
                    fragmentTransactionNews.replace(fragmentContainer.id, newsFragment)
                    fragmentTransactionNews.commit()
                }
                R.id.profile -> {
                    FirebaseAnalytics.getInstance(this).logEvent("PROFILE_TabClicked", null)

                    val profileFragment = Login_Fragment()
                    val fragmentTransaction = supportFragmentManager.beginTransaction()
                    fragmentTransaction.replace(fragmentContainer.id, profileFragment)
                    fragmentTransaction.commit()

                }

            }
            true
        }


    }
}
