package com.example.myapplicationaaa1.fragment


import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.example.myapplicationaaa1.R
import com.example.myapplicationaaa1.activity.RegisterActivity
import com.example.myapplicationaaa1.utils.UserDao
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.fragment_profile.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class LoginActivity_Fragment : Fragment() {




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
        ): View? {
        val view: View = inflater!!.inflate(R.layout.fragment_profile, container, false)

        // Inflate the layout for this fragment
        return view;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        logoutButton.visibility=View.GONE
        registerButton.visibility=View.VISIBLE
        LogIn.visibility=View.VISIBLE

        LogIn.setOnClickListener()
        {
            LoginUser()
        }
        registerButton.setOnClickListener{
            startActivity(Intent(requireContext(), RegisterActivity::class.java))
        }
    }


    private fun LoginUser() {

        val username = usernameEditText.text.toString()
        val password = passwordEditText.text.toString()

        if (!username.isEmpty()) {
            Toast.makeText(context, "Please Enter UserName", Toast.LENGTH_LONG).show()
            return
        }
        else if (!password.isEmpty()) {
            Toast.makeText(context, "Please Enter Password", Toast.LENGTH_LONG).show()
            return
        }
        else { //Progress bar
            //Login aND INIT
            if(FirebaseAuth.getInstance().currentUser==null){//no hay usera logeado
                //check en la base de datos
            }
            else
            {
                registerButton.visibility=View.GONE
                LogIn.visibility=View.GONE
                logoutButton.visibility=View.VISIBLE

                logoutButton.setOnClickListener{
                    FirebaseAuth.getInstance().signOut()
                    //Show success to user
                    LoginUser()

                }
                //Le ponemos ? let porque es nullable
                FirebaseAuth.getInstance().currentUser?.uid?.let {userID->
                    UserDao().get(userID,
                        successListener = {user ->
                            //usernameTextView.text=user?.userName
                        },
                        failureListener = {
                            //toast? el usuario no lo necesita
                        })
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
                Toast.makeText(requireContext(),it.localizedMessage,Toast.LENGTH_LONG).show()
            }
        )
    }
    private fun getUser(){
        UserDao().get (userId = "userID",
            successListener ={

            },
            failureListener ={

            } )
    }

//    private fun updateUser(){
//        UserDao().update(
//
//        )
//    }

}
