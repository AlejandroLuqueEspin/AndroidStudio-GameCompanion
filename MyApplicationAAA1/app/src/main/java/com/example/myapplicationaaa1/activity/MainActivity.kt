package com.example.myapplicationaaa1.activity

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.myapplicationaaa1.R
import com.example.myapplicationaaa1.fragment.Login_Fragment
import com.example.myapplicationaaa1.fragment.News_Fragment
import com.example.myapplicationaaa1.fragment.Streams_Fragment
import com.google.android.gms.location.*
import com.google.firebase.analytics.FirebaseAnalytics
import kotlinx.android.synthetic.main.activity_main.*
import com.google.android.gms.location.LocationServices
import com.google.android.gms.common.api.GoogleApiClient
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.os.Handler
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import com.example.myapplicationaaa1.model.NewsList
import com.example.myapplicationaaa1.model.NewsModel
import com.example.myapplicationaaa1.utils.UserDao
import kotlinx.android.synthetic.main.fragment_news.*


class MainActivity : AppCompatActivity() {

    lateinit var  mLocRequest: LocationRequest

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback

    var mCurrentLocation:Location?=null



    var runnable: Runnable = Runnable { metodo_timer() }
    var contador = 0
    var handler = Handler()




    override fun onCreate(savedInstanceState: Bundle?) {
        runnable.run();

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FirebaseAnalytics.getInstance(this).logEvent("AppOpen", null)

        //Para inicar el gragmentlayout con algo al inicio
        val profileFragment = Login_Fragment()
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.add(fragmentContainer.id, profileFragment)
        fragmentTransaction.commit()

        setupPermissions()

        //LOCATION STUFF


        mLocRequest = LocationRequest.create()
        mLocRequest.setInterval(1000);
        mLocRequest.setSmallestDisplacement(5f)
        mLocRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(mLocRequest)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)


        locationCallback = object : LocationCallback() {


            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                for (location in locationResult.locations){

                    if(bottomNavView.visibility== VISIBLE)
                        bottomNavView.visibility=GONE
                    if(bottomNavView.visibility== GONE)
                        bottomNavView.visibility=VISIBLE

                    if(mCurrentLocation==null) {
                        mCurrentLocation = location
                        showNotification("Has Iniciado la App", "Guardando la localizacion")
                    }
                    else if(location.altitude!= mCurrentLocation!!.altitude){
                        mCurrentLocation = location
                        showNotification("Te has movido?", "Abre la app y mira los ultimos posts")
                    }


                }
            }
        }

        //startLocationUpdates()

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


    private fun startLocationUpdates() {
        fusedLocationClient.requestLocationUpdates(mLocRequest,
            locationCallback,
            null )
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

    fun metodo_timer() {

        contador++;
        if (contador > 3) {
            contador = 0
            startLocationUpdates()

        }


        handler.postDelayed(runnable, 1000);
    }
}


