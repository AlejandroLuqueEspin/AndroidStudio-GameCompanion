package com.example.myapplicationaaa1.activity

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.myapplicationaaa1.R
import com.example.myapplicationaaa1.fragment.Login_Fragment
import com.example.myapplicationaaa1.fragment.News_Fragment
import com.example.myapplicationaaa1.fragment.Streams_Fragment
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

        setupPermissions()

        bottomNavView.setOnNavigationItemSelectedListener { menuItem ->
            //            val parametres =Bundle()
//                parametres.putString("tab","chat")

            when (menuItem.itemId) {
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
                R.id.streams ->{
                    FirebaseAnalytics.getInstance(this).logEvent("PROFILE_TabClicked",null)

                    val profileFragment= Streams_Fragment()
                    val fragmentTransaction =supportFragmentManager.beginTransaction()
                    fragmentTransaction.replace(fragmentContainer.id,profileFragment)
                    fragmentTransaction.commit()

                }

            }
            true
        }


    }


    //request
    val RECORD_REQUEST_CODE = 101


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            RECORD_REQUEST_CODE -> {

                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {

                    Log.i(TAG, "Permission has been denied by user")
                } else {
                    Log.i(TAG, "Permission has been granted by user")
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private val TAG = "Permission"

    private fun setupPermissions() {
        val permission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Permission to record denied")
            makeRequest()

        }
    }
    private fun makeRequest() {
        ActivityCompat.requestPermissions(this,
            arrayOf(android.Manifest.permission.RECORD_AUDIO),
            RECORD_REQUEST_CODE)
    }
}
