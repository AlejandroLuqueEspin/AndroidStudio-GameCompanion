package com.example.myapplicationaaa1.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.example.myapplicationaaa1.R
import com.example.myapplicationaaa1.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {
    // get reference to button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        registerButton.setOnClickListener{
            createNewUser()
        }

    }




private fun createNewUser(){

        val username=usernameEditText.text.toString()
        val email=emailEditText.text.toString()
        val password=passwordEditText.text.toString()

        if(username.isEmpty()) {
            Toast.makeText(this,"Please Enter UserName", Toast.LENGTH_LONG).show()
            return
        }
        else if(email.isEmpty()){
            Toast.makeText(this,"Please Enter Email", Toast.LENGTH_LONG).show()
            return
        }

        else if(password.isEmpty()){
            Toast.makeText(this,"Please Enter Password", Toast.LENGTH_LONG).show()
            return
        }

        else{
            //Progress bar
            //Register
            val dataBase= FirebaseFirestore.getInstance();
            val newUser=UserModel("",
                username,
                password,
                email
            )
            dataBase
                .collection("Users")
                .add(newUser)
                .addOnSuccessListener {documentReference ->
                    Toast.makeText(this,"Register Complete", Toast.LENGTH_LONG).show()
                }.addOnFailureListener { e ->
                    Toast.makeText(this,"Error, try again later", Toast.LENGTH_LONG).show()
                }

        }


    }




}
