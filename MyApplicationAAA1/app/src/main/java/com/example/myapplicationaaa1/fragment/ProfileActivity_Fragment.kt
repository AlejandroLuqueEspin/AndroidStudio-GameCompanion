package com.example.myapplicationaaa1.fragment

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import kotlinx.android.synthetic.main.fragment_profileuser.*

import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myapplicationaaa1.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream

class ProfileActivity_Fragment : Fragment() {

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
            if (FirebaseAuth.getInstance().currentUser != null) {
                FirebaseAuth.getInstance().signOut()
                UpdateUI()

            }
        }
        imageProfile.setOnClickListener() {
            takePicture()
        }


    }

    private fun UpdateUI() {
        if (FirebaseAuth.getInstance().currentUser == null) {
            //Change fragment
            val newFragment = LoginActivity_Fragment()
            val fragmentTransaction = activity?.supportFragmentManager?.beginTransaction()
            fragmentTransaction?.replace(R.id.fragmentContainer, newFragment)
            fragmentTransaction?.commit()
        }
    }

    //Photo
    private fun takePicture() {
        val cameraIntent=Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        activity?.let {
            cameraIntent.resolveActivity(requireActivity().packageManager)
            startActivityForResult(cameraIntent,10)
        }
    }
    //El 10 deberia estar en un archivo de constant
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==10&&resultCode==Activity.RESULT_OK){
            //Camera Result
            val image=data?.extras?.get("data") as? Bitmap
            image?.let {
                imageProfile.setImageBitmap(it)
                saveImageToCloud(it)


            }
        }
    }

    private fun saveImageToCloud(image:Bitmap){
        val baos=ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.JPEG,100,baos)
        //Create reference
        val folderReference=FirebaseStorage.getInstance().reference.child("public/images/avatars/")
        val imageReference=folderReference.child("avatar.jpg")//mAKE UNIQUE NAME
        imageReference.putBytes(baos.toByteArray()).addOnSuccessListener {
            imageReference.downloadUrl.addOnSuccessListener { uri ->
                val url=uri.toString()
                //uPDATE IT ON USER FIRE BASE
            }
        }.addOnFailureListener{

        }
    }
    //
}



