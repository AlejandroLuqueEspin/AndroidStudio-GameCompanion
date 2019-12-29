package com.example.myapplicationaaa1.activity

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.myapplicationaaa1.R
import com.example.myapplicationaaa1.fragment.Login_Fragment
import com.example.myapplicationaaa1.fragment.News_Fragment
import com.example.myapplicationaaa1.fragment.Streams_Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.analytics.FirebaseAnalytics
import kotlinx.android.synthetic.main.activity_main.*



class MainActivity : AppCompatActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient


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



        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location : Location? ->
                // Got last known location. In some rare situations this can be null.
                if(location!=null)
                    showNotification("PENE?","penesito si señor")
            }


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
        val permission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Permission to record denied")
            makeRequest()

        }
    }
    private fun makeRequest() {
        ActivityCompat.requestPermissions(this,
            arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION),
            RECORD_REQUEST_CODE)
    }


//fun createNotification(){
//
//    val notificationId=101
//
//    // Create an explicit intent for an Activity in your app
//    val intent = Intent(this, MainActivity::class.java).apply {
//        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//    }
//    val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)
//
//    var builder = NotificationCompat.Builder(this, CHANNEL_ID)
//        .setSmallIcon(R.drawable.ic_icon)
//        .setContentTitle("Are you moving?")
//        .setContentText("I warn you if you prefer to stay playing ff14 on your pc")
//        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//        // Set the intent that will fire when the user taps the notification
//        .setContentIntent(pendingIntent)
//        .setAutoCancel(true)
//
//    with(NotificationManagerCompat.from(this)) {
//        // notificationId is a unique int for each notification that you must define
//        notify(notificationId, builder.build())
//    }
//}
//    private fun createNotificationChannel() {
//        // Create the NotificationChannel, but only on API 26+ because
//        // the NotificationChannel class is new and not in the support library
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            // Create the NotificationChannel
//            val name = "Location Notification"
//            val descriptionText = "Gets if you move"
//            val importance = NotificationManager.IMPORTANCE_DEFAULT
//            val mChannel = NotificationChannel(chanel, name, importance)
//            mChannel.description = descriptionText
//            // Register the channel with the system; you can't change the importance
//            // or other notification behaviors after this
//            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
//            notificationManager.createNotificationChannel(mChannel)
//        }
//    }
    fun showNotification(title: String, message: String) {
        val mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel("YOUR_CHANNEL_ID",
                "Location Notification",
                NotificationManager.IMPORTANCE_DEFAULT)
            channel.description = "Are you moving?"
            mNotificationManager.createNotificationChannel(channel)
        }
        val mBuilder = NotificationCompat.Builder(applicationContext, "YOUR_CHANNEL_ID")
            .setSmallIcon(R.mipmap.ic_launcher_round_ff) // notification icon
            .setContentTitle(title) // title for notification
            .setContentText(message)// message for notification
            .setAutoCancel(true) // clear notification after click
        val intent = Intent(applicationContext, MainActivity::class.java)
        val pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        mBuilder.setContentIntent(pi)
        mNotificationManager.notify(0, mBuilder.build())
    }


}
