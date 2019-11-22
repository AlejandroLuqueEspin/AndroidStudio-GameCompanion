package com.example.myapplicationaaa1.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.NonNull
import com.example.myapplicationaaa1.ProfileFragment
import com.example.myapplicationaaa1.R
import com.example.myapplicationaaa1.model.UserModel
import com.google.android.gms.dynamic.IFragmentWrapper
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.fragment_profile.*

class RegisterActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

    }


private fun createNewUser(){
    val username=usernameEditText.text.toString()
    val email=emailEditText.text.toString()
    val password=passwordEditText.text.toString()

    if(!username.isEmpty()) {
        Toast.makeText(this,"Please Enter UserName", Toast.LENGTH_LONG).show()
        return
    }
    else if(!email.isEmpty()){
        Toast.makeText(this,"Please Enter Email", Toast.LENGTH_LONG).show()
        return
    }

    else if(!password.isEmpty()){
        Toast.makeText(this,"Please Enter Password", Toast.LENGTH_LONG).show()
        return
    }

    else{
        //Progress bar
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener{
                if(it.isSuccessful()){
                    startActivity(Intent(this, ProfileFragment::class.java))

                }else{
                    Toast.makeText(this,"Please Try Again Later", Toast.LENGTH_LONG).show()
                }


    }




    }




}






}
