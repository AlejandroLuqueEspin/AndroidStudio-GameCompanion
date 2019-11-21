package com.example.myapplicationaaa1.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.myapplicationaaa1.R
import com.example.myapplicationaaa1.model.UserModel
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
    }
    val username=usernameEditText.text.toString()
    val email=emailEditText.text.toString()
    val password=passwordEditText.text.toString()



    //2 VALIDATIONS
    //VALIDATE USERNAME
    if(username.isBlank()){

    }
    //VALIDATE EMAIL
    if(email.isBlank()||!Patterns.EMAIL_ADRESS.matcher(email).matches()){

    }
    //VALIDATE PASSWORD
    if(password.isBlank()||!isPasswordValid(password)){

    }

    //3 send to firebase
    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password).addOnSuccessListener
    {authResult->



        //create user profile
        val user=
            UserModel(
                userID = authResult.user?.uid,
                email = email,
                userName = username
            )
        FirebaseFirestore.getInstance()
            .collection("users")
            .document(authResult.user?.uid?:"")//si no existe te lo crea vacio
            .set(user).addOnSuccessListener {
                //Success
                Toast.makeText(usernameEditText.context,
                    "User Created Successfully!",
                    Toast.LENGTH_LONG
                ).show()
                //closeActivity
                finish()

            }.addOnFailureListener{
                Toast.makeText(usernameEditText.context,
                    it.localizedMessage,
                    Toast.LENGTH_LONG).show()
            }


    }.addOnFailureListener{
        Toast.makeText(usernameEditText.context,it.localizeMessage,Toast.LENGTH_LONG).show()
    }

    fun isPasswordValid(password: String):Boolean{

        return true
    }




}
