package com.example.myapplicationaaa1.activity

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.myapplicationaaa1.R
import com.example.myapplicationaaa1.model.NewsModel
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_add_post.*
import kotlinx.android.synthetic.main.fragment_profileuser.*
import kotlinx.android.synthetic.main.item_news.*
import kotlinx.android.synthetic.main.item_news.userNameView
import java.util.*

class AddPostActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_post)
        progressBar10.visibility = View.GONE

        UpdateUI()

        finishPostButton.setOnClickListener{
            //analythics
            createNewPost()
        }

    }

    private fun UpdateUI(){

        //GET USER LOCALLY
        val username = applicationContext.getSharedPreferences("userProfile", Context.MODE_PRIVATE)
            .getString("username", null)

        val url = applicationContext.getSharedPreferences("userProfile", Context.MODE_PRIVATE)
            .getString("image_url", null)

        usernamePost.text = username

        if (url != null)
            Glide.with(userPhoto.context).load(url).into(userPhoto)


    }

    private fun createNewPost() {

        val postText = postTextPostActivity.text.toString()

        if (postText.isEmpty()) {
            Toast.makeText(this, "Please Enter some Text to Post", Toast.LENGTH_LONG).show()
            return

        } else {
            //Progress bar
            progressBar10.visibility = View.VISIBLE

            val dataBase =FirebaseFirestore.getInstance()

            val newPost= NewsModel(
                textPosted = postText,
                imageUserUrl =applicationContext.getSharedPreferences("userProfile", Context.MODE_PRIVATE)
                .getString("image_url", null) ,
                userName = applicationContext.getSharedPreferences("userProfile", Context.MODE_PRIVATE)
                .getString("username", null))
            val idPost= UUID.randomUUID().toString()

                FirebaseAuth.getInstance().currentUser?.uid?.let {userUid->

                    dataBase
                        .collection("Posts")
                        .document(idPost)
                        .set(newPost)
                        .addOnSuccessListener { documentReference ->

                            dataBase
                                .collection("Users").document(userUid).collection("posts").document(idPost).set(newPost).addOnSuccessListener {

                                    Toast.makeText(this, "Post Complete", Toast.LENGTH_LONG)
                                        .show()
                                    progressBar10.visibility = View.GONE
                                    this.finish()
                                }.addOnFailureListener{
                                    Toast.makeText(
                                        this,
                                        "Error, storing the post in user posts",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    progressBar10.visibility = View.GONE
                                }

                        }.addOnFailureListener { e ->
                            Toast.makeText(
                                this,
                                "Error, try again later",
                                Toast.LENGTH_LONG
                            ).show()
                            progressBar10.visibility = View.GONE

                        }
            }




        }


    }

}
