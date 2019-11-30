package com.example.myapplicationaaa1.fragment

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import java.util.UUID;
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.myapplicationaaa1.R
import com.example.myapplicationaaa1.utils.UserDao
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.ContentViewCallback
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment_profileuser.*
import java.io.ByteArrayOutputStream
import java.net.URL
import java.util.jar.Manifest


private val REQUEST_IMAGE_CAPTURE = 1;

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
        SharedPreferencesFunction()
        logoutButton.setOnClickListener()
        {
            if (FirebaseAuth.getInstance().currentUser != null) {
                FirebaseAuth.getInstance().signOut();
                UpdateUI()

            }
        }
        imageProfile.setOnClickListener() {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
            FirebaseAnalytics.getInstance(this.requireActivity()).logEvent("Take_photo",null)

        }
    }

    private fun UpdateUI() {
        if (FirebaseAuth.getInstance().currentUser == null) {
            //Change fragment
            val newFragment = Login_Fragment()
            val fragmentTransaction = activity?.supportFragmentManager?.beginTransaction()
            fragmentTransaction?.replace(R.id.fragmentContainer, newFragment)
            fragmentTransaction?.commit()
        }
    }


    private fun SharedPreferencesFunction() {
        //GET USER LOCALLY
        val username = requireContext().getSharedPreferences("userProfile", Context.MODE_PRIVATE)
            .getString("username", null)
        val password = requireContext().getSharedPreferences("userProfile", Context.MODE_PRIVATE)
            .getString("password", null)
        val email = requireContext().getSharedPreferences("userProfile", Context.MODE_PRIVATE)
            .getString("email", null)
        val url = requireContext().getSharedPreferences("userProfile", Context.MODE_PRIVATE)
            .getString("image_url", null)

        userNameView.text = username
        userPasswordView.text = password
        userEmailView.text = email

        if (url != null)
            Glide.with(imageProfile.context).load(url).into(imageProfile)

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if (resultCode == Activity.RESULT_OK) {

                val bmp: Bitmap = data?.getExtras()?.get("data") as Bitmap;
                val stream = ByteArrayOutputStream();

                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                val byteArray = stream.toByteArray()


                val bitmap: Bitmap = BitmapFactory.decodeByteArray(
                    byteArray, 0,
                    byteArray.size
                );

                imageProfile.setImageBitmap(bitmap)

                // Get the data from an ImageView as bytes
                val generatedName: String = UUID.randomUUID().toString()
                val mountainImagesRef = FirebaseStorage.getInstance()
                    .reference.child("public/images/avatars/" + generatedName)

                val bitmapAux = (imageProfile.drawable as BitmapDrawable).bitmap
                val baos = ByteArrayOutputStream()
                bitmapAux.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                val data = baos.toByteArray()

                var uploadTask = mountainImagesRef.putBytes(data)
                uploadTask.addOnFailureListener {
                    Toast.makeText(context, "Error Uploading the image", Toast.LENGTH_LONG).show()
                }.addOnSuccessListener { it ->

                    it.metadata!!.reference!!.downloadUrl.addOnSuccessListener { task ->
                        //save url in user url
                        FirebaseAuth.getInstance().currentUser?.uid?.let { uid ->
                            FirebaseFirestore
                                .getInstance()
                                .collection("Users")
                                .document(uid)
                                .update("url", task.toString())


                        }


                    }.addOnFailureListener {

                    }

                }

            }
        }
    }

}



