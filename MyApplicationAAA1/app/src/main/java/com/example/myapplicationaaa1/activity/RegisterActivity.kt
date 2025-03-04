package com.example.myapplicationaaa1.activity

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplicationaaa1.R
import com.example.myapplicationaaa1.model.UserModel
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        progressBar3.visibility = View.GONE

        registerButton.setOnClickListener {
            createNewUser()
            FirebaseAnalytics.getInstance(this).logEvent("Register_Try", null)

        }


    }


    private fun createNewUser() {

        val username = usernameEditText.text.toString()
        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()

        if (username.isEmpty()) {
            Toast.makeText(this, "Please Enter UserName", Toast.LENGTH_LONG).show()
            return
        } else if (email.isEmpty()) {
            Toast.makeText(this, "Please Enter Email", Toast.LENGTH_LONG).show()
            return
        } else if (password.isEmpty()) {
            Toast.makeText(this, "Please Enter Password", Toast.LENGTH_LONG).show()
            return
        } else {
            //Progress bar
            progressBar3.visibility = View.VISIBLE

            //Register with
            val auth = FirebaseAuth.getInstance()
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        val user = auth.currentUser

                        //Aqui???Register in FiraStore
                        val dataBase = FirebaseFirestore.getInstance()
                        val newUser = UserModel(
                            userID = "",
                            //stroe this into a constant
                            userName = username,
                            url = "https://firebasestorage.googleapis.com/v0/b/alejandroluquegamecompanion.appspot.com/o/public%2Fimages%2Favatars%2Fic_ImageProfileDefault-web.png?alt=media&token=82231fc0-1e0d-4ffe-b4ac-f4677ee8e64b",
                            password = password,
                            email = email
                        )
                        if (user !== null) {
                            dataBase
                                .collection("Users")
                                .document(user.uid)
                                .set(newUser)
                                .addOnSuccessListener { documentReference ->
                                    Toast.makeText(this, "Register Complete", Toast.LENGTH_LONG)
                                        .show()
                                    progressBar3.visibility = View.GONE
                                    FirebaseAnalytics.getInstance(this)
                                        .logEvent("Register_Complete", null)

                                    applicationContext.getSharedPreferences(
                                        "userProfile",
                                        Context.MODE_PRIVATE
                                    ).edit()
                                        .putString("username", username).apply()
                                    applicationContext.getSharedPreferences(
                                        "userProfile",
                                        Context.MODE_PRIVATE
                                    ).edit()
                                        .putString("password", password).apply()
                                    applicationContext.getSharedPreferences(
                                        "userProfile",
                                        Context.MODE_PRIVATE
                                    ).edit()
                                        .putString("email", email).apply()
                                    applicationContext.getSharedPreferences(
                                        "userProfile",
                                        Context.MODE_PRIVATE
                                    ).edit()
                                        .putString(
                                            "image_url",
                                            "https://firebasestorage.googleapis.com/v0/b/alejandroluquegamecompanion.appspot.com/o/public%2Fimages%2Favatars%2Fic_ImageProfileDefault-web.png?alt=media&token=82231fc0-1e0d-4ffe-b4ac-f4677ee8e64b"
                                        ).apply()
                                    progressBar3.visibility = View.GONE
                                    this.finish()

                                }.addOnFailureListener { e ->
                                    Toast.makeText(
                                        this,
                                        "Error, try again later",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    progressBar3.visibility = View.GONE

                                }
                        }

                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(this, "Authentication failed.", Toast.LENGTH_LONG).show()
                        progressBar3.visibility = View.GONE

                    }
                }.addOnFailureListener() {
                    Toast.makeText(
                        this,
                        "Error, try to increase the username or password",
                        Toast.LENGTH_LONG
                    ).show()

                    progressBar3.visibility = View.GONE

                }


        }


    }


}
