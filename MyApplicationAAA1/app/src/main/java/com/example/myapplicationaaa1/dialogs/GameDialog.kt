package com.example.myapplicationaaa1.dialogs


import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatDialogFragment
import com.bumptech.glide.Glide
import com.example.myapplicationaaa1.model.TWGameModel
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import android.widget.ImageView
import java.io.IOException
import java.net.HttpURLConnection


class GameDialog(val game_id:TWGameModel) : AppCompatDialogFragment() {


     var gameData:TWGameModel=game_id


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val builder = AlertDialog.Builder(activity)
        builder.setTitle(gameData.name)
            .setPositiveButton("OK") { dialogInterface, i ->
            }
            .setNegativeButton("CANCEL") { dialogInterface, i ->
            }



        return builder.create()
    }
    fun getBitmapFromURL(src: String): Bitmap? {
        try {
            val url = java.net.URL(src)
            val connection = url
                .openConnection() as HttpURLConnection
            connection.setDoInput(true)
            connection.connect()
            val input = connection.getInputStream()
            return BitmapFactory.decodeStream(input)
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }

    }
}