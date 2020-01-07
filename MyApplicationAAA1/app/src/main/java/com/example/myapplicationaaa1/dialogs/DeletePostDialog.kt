package com.example.myapplicationaaa1.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatDialogFragment
import com.example.myapplicationaaa1.fragment.News_Fragment
import com.google.firebase.firestore.FirebaseFirestore

 class DeletePostDialog(val postUID:String,val UPpostUID:String?,val commented:Boolean) : AppCompatDialogFragment() {


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle("Confirm Delete Post")
            .setMessage("Are you sure you want to delete this post? This cannot be undone.")
            .setPositiveButton("OK") { dialogInterface, i ->
                //GetThePost
                if(commented){
                    postUID?.let { checkedPostUID ->
                        FirebaseFirestore.getInstance()
                            .collection("Posts")
                            .document(UPpostUID!!)
                            .collection("comments")
                            .document(postUID)
                            .delete()
                            .addOnSuccessListener { documentSnapshot ->

                            }.addOnFailureListener { e ->

                            }
                    }
                }
                else {
                    postUID?.let { checkedPostUID ->
                        FirebaseFirestore.getInstance()
                            .collection("Posts")
                            .document(checkedPostUID)
                            .delete()
                            .addOnSuccessListener { documentSnapshot ->

                            }.addOnFailureListener { e ->


                            }
                    }
                }
                }
            .setNegativeButton("CANCEL") { dialogInterface, i ->

            }

        return builder.create()
    }
}