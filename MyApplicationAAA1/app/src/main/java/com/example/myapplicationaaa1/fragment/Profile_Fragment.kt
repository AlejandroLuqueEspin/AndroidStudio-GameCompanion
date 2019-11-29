package com.example.myapplicationaaa1.fragment

import android.content.Context
import android.content.SharedPreferences
import kotlinx.android.synthetic.main.fragment_profileuser.*

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myapplicationaaa1.R
import com.google.firebase.auth.FirebaseAuth

class Profile_Fragment : Fragment() {

    override fun onResume() {
        super.onResume()
        UpdateUI()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater!!.inflate(R.layout.fragment_profileuser, container, false)

        // Inflate the layout for this fragment
        return view;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        logoutButton.setOnClickListener()
        {
            if (FirebaseAuth.getInstance().currentUser != null)
            {
                FirebaseAuth.getInstance().signOut();
                UpdateUI()

            }
        }
    }

    private fun UpdateUI(){
        if(FirebaseAuth.getInstance().currentUser==null){
            //Change fragment
            val newFragment= Login_Fragment()
            val fragmentTransaction =activity?.supportFragmentManager?.beginTransaction()
            fragmentTransaction?.replace(R.id.fragmentContainer,newFragment)
            fragmentTransaction?.commit()
        }
    }


    private fun SharedPreferencesFunction(){
        //GET USER LOCALLY
        val username =requireContext()
            .getSharedPreferences("userProfile", Context.MODE_PRIVATE).getString("username",null)


        userNameView.text=username
    }






}



