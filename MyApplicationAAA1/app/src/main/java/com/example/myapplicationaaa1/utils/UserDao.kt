package com.example.myapplicationaaa1.utils

import com.example.myapplicationaaa1.model.UserModel
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.FirebaseFirestore
import java.lang.Exception

class UserDao {

    //PARAM UASERID, SuccessLISTENER AND FailLISTENER
    fun get(userId: String,
            successListener: (user:UserModel?)->(Unit),//Unit==void
            failureListener: (error: Exception)->Unit){

        FirebaseFirestore.getInstance()
            .collection("Users")
            .document(userId)
            .get()
            //ON SUCCESS
            .addOnSuccessListener {documentSnapshot->
                //CODE ON SUCCESS (THE BASEDATA REQUEST)
                val user = documentSnapshot.toObject(UserModel::class.java)
                if(user==null){
                }
                else {
                    //LLAMAMOS AL SUCCESSLISTENER DE FUERA Y LE PASAMOS LA VARIABLE
                    successListener(user)
                }
            }
            .addOnFailureListener{
                //CODE ON FAIL
                failureListener(it)
            }

    }

    fun update(user: UserModel,
               successListener: ()->(Unit),//Unit==void
               failureListener: (error: Exception)->Unit){

        user.userID?.let {userID->
            FirebaseFirestore.getInstance()
                .collection("users")
                .document(userID)
                //PREGUNTAR COMO SE FUNCIONA
                .set(user)
                //ON SUCCESS
                .addOnSuccessListener {documentSnapshot->

                    successListener()
                }
                .addOnFailureListener{
                    //CODE ON FAIL
                    failureListener(it)
                }
        } ?: run{
            failureListener(Exception("User Id was Null"))
        }

    }

    fun getAll(successListener: (users:List<UserModel>)->(Unit),
               failureListener: (error: Exception)->Unit){// un parametro bloc (bloque de codigo), sera de tipo list que le pasamos unos users

        FirebaseFirestore.getInstance()
            .collection("users")
            //get all documents
            .get()
            //On success
            .addOnSuccessListener { querySnapshot ->
                //QuerysBAPSHOTS CONTAINS DOCUMENTS AND METADATA
                val documents=querySnapshot.documents
                val userList=ArrayList<UserModel>()
                //Prepare users List
                documents.forEach{documentSnapshot->
                    //document snapshot contains data and metadata
                    val user=documentSnapshot.toObject(UserModel::class.java)
                   //take only if not null
                    user?.let{
                        userList.add(user)
                    }

                }
                //Call bloc (Listener)
                successListener(userList)
            }
            .addOnFailureListener{
                failureListener(it)
            }
    }

    fun registerUser(user: UserModel,
                     successListener: (users:List<UserModel>)->(Unit),
                     failureListener: (error: Exception)->Unit){

    }
}