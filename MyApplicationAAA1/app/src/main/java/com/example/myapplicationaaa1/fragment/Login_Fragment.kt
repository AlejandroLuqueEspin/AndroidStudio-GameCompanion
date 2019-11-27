package com.example.myapplicationaaa1.fragment


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.myapplicationaaa1.R
import com.example.myapplicationaaa1.activity.RegisterActivity
import com.example.myapplicationaaa1.utils.UserDao
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_login.passwordEditText
import kotlinx.android.synthetic.main.fragment_login.registerButton


class Login_Fragment : Fragment() {

    override fun onResume() {
        super.onResume()
        UpdateUI()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
        ): View? {
        val view: View = inflater!!.inflate(R.layout.fragment_login, container, false)

        return view;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registerButton.visibility=View.VISIBLE
        LogIn.visibility=View.VISIBLE

        LogIn.setOnClickListener()
        {
            LoginUser()
        }
        registerButton.setOnClickListener{
            if(FirebaseAuth.getInstance().currentUser==null)
                startActivity(Intent(requireContext(), RegisterActivity::class.java))
            UpdateUI()

        }

        UpdateUI()
    }

    private fun UpdateUI(){
        if(FirebaseAuth.getInstance().currentUser==null){
            registerButton.visibility=View.VISIBLE
            LogIn.visibility=View.VISIBLE
        }
        else
        {
            registerButton.visibility=View.GONE
            LogIn.visibility=View.GONE

            //Change fragment
            val newFragment= Profile_Fragment()
            val fragmentTransaction =activity?.supportFragmentManager?.beginTransaction()
            fragmentTransaction?.replace(R.id.fragmentContainer,newFragment)
            fragmentTransaction?.commit()
        }

    }

    private fun LoginUser() {

        val password = passwordEditText.text.toString()
        val email = emailLoginEditText.text.toString()

        if (email.isEmpty()) {
            Toast.makeText(context, "Please Enter Email", Toast.LENGTH_LONG).show()
            return
        }
        else if (password.isEmpty()) {
            Toast.makeText(context, "Please Enter Password", Toast.LENGTH_LONG).show()
            return
        }
        else { //Progress bar
            //Login aND INIT
            if(FirebaseAuth.getInstance().currentUser==null){//no hay usera logeado
                //check en la base de datos
                val auth=FirebaseAuth.getInstance()
                auth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener{
//                    auth.currentUser?.uid?.let {userID->
//                        getUser(userID)
//                    }
                    UpdateUI()
                }.addOnFailureListener{
                    Toast.makeText(context, "Error with sing in", Toast.LENGTH_LONG).show()
                }


            }
        }

    }



    private fun getAllUsersTest(){
        UserDao().getAll (
            successListener = {users->
                //adapter.users=users

            },
            failureListener={
                Toast.makeText(context, "Los datos de usuario/contraseñoa son incorrectos", Toast.LENGTH_LONG).show()

                Toast.makeText(requireContext(),it.localizedMessage,Toast.LENGTH_LONG).show()
            }
        )
    }
    private fun getUser(userId:String){
        UserDao().get (userId = userId,
            successListener ={

//                userConsulted->
////                userConsulted.let {
////                    it?.password?.let {password->
////                        passwordEditText.text=password
////                    }
////                    it?.email?.let {email->
////                        emailLoginEditText.text=email
////                    }
////                }

                Toast.makeText(context, "Los datos de usuario/contraseñoa son correctos", Toast.LENGTH_LONG).show()

            },
            failureListener ={
                Toast.makeText(context, "Los datos de usuario/contraseñoa son incorrectos", Toast.LENGTH_LONG).show()


            } )
    }

//    private fun updateUser(){
//        UserDao().update(
//
//        )
//    }

}
