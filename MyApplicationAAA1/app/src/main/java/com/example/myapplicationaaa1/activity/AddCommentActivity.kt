package com.example.myapplicationaaa1.activity

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.myapplicationaaa1.R
import com.example.myapplicationaaa1.model.NewsModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_add_comment.*
import java.util.*

class AddCommentActivity : AppCompatActivity() {

    val commitedPostUserUid = null
    var postUID: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_comment)
        progressBarAddComment.visibility = View.GONE

        UpdateUI()

        val b = getIntent().getExtras()
        if (b != null) {
            postUID = b.getString("postUID")
        } else
            finish()

        finishCommentButton.setOnClickListener {
            //analythics
            createNewPost()
        }

    }

    private fun UpdateUI() {

        //GET USER LOCALLY
        val username = applicationContext.getSharedPreferences("userProfile", Context.MODE_PRIVATE)
            .getString("username", null)

        val url = applicationContext.getSharedPreferences("userProfile", Context.MODE_PRIVATE)
            .getString("image_url", null)

        usernameComment.text = username

        if (url != null)
            Glide.with(userPhotoComment.context).load(url).into(userPhotoComment)


    }

    private fun createNewPost() {

        val postText = commentTextCommentActivity.text.toString()

        if (postText.isEmpty()) {
            Toast.makeText(this, "Please Enter some Text to Post", Toast.LENGTH_LONG).show()
            return

        } else {
            //Progress bar
            progressBarAddComment.visibility = View.VISIBLE

            val dataBase = FirebaseFirestore.getInstance()

            val newPost = NewsModel(
                postUID =  UUID.randomUUID().toString(),
                userUID = FirebaseAuth.getInstance().currentUser?.uid,
                textPosted = postText,
                upPostUID = postUID,
                imageUserUrl = applicationContext.getSharedPreferences(
                    "userProfile",
                    Context.MODE_PRIVATE
                )
                    .getString("image_url", null),
                userName = applicationContext.getSharedPreferences(
                    "userProfile",
                    Context.MODE_PRIVATE
                )
                    .getString("username", null)
            )
            val idPost = newPost.postUID


            FirebaseAuth.getInstance().currentUser?.uid?.let { userUid ->

                if (idPost != null) {
                    postUID?.let {
                        dataBase
                            .collection("Posts").document(it).collection("comments").document(idPost)
                            .set(newPost).addOnSuccessListener {

                                Toast.makeText(this, "Post Complete", Toast.LENGTH_LONG)
                                    .show()
                                progressBarAddComment.visibility = View.GONE
                                this.finish()
                            }.addOnFailureListener {
                                Toast.makeText(
                                    this,
                                    "Error, storing the post in user posts",
                                    Toast.LENGTH_LONG
                                ).show()
                                progressBarAddComment.visibility = View.GONE
                            }
                    }
                }

            }


        }


    }
}
