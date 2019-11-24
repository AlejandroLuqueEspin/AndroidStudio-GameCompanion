package com.example.myapplicationaaa1.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myapplicationaaa1.R
import com.example.myapplicationaaa1.fragment.LoginActivity_Fragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Para inicar el gragmentlayout con algo al inicio
        val profileFragment= LoginActivity_Fragment()
        val fragmentTransaction =supportFragmentManager.beginTransaction()
        fragmentTransaction.add(fragmentContainer.id,profileFragment)
        fragmentTransaction.commit()


        bottomNavView.setOnNavigationItemSelectedListener {
            menuItem->
            when(menuItem.itemId){
                R.id.chat ->{}
                R.id.home ->{}
                R.id.profile ->{
                    val profileFragment= LoginActivity_Fragment()
                    val fragmentTransaction =supportFragmentManager.beginTransaction()
                    fragmentTransaction.replace(fragmentContainer.id,profileFragment)
                    fragmentTransaction.commit()

                }

            }
            true
        }




    }
}
