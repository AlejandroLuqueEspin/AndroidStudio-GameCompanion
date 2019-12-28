package com.example.myapplicationaaa1.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplicationaaa1.R
import com.example.myapplicationaaa1.model.NewsModel
import com.example.myapplicationaaa1.model.UserModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.item_news.view.*
import kotlin.coroutines.coroutineContext


class NewsAdapter(var listOfNews: ArrayList<NewsModel>) :

    RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

    class ViewHolder(item: View) : RecyclerView.ViewHolder(item) {

        val username: TextView = item.userNameView
        val textPosted: TextView = item.textPosted
        val imageUserView: ImageView = item.userImageView
    }


    override fun getItemCount(): Int {
        return listOfNews.count()
    }

    // Create item_joke View
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val item: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_news, parent, false)
        return ViewHolder(item)
    }

    // Update Items
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val news = listOfNews[position]

        holder.username.text = news.userName
        holder.textPosted.text = news.textPosted
        // 1 - Download image from URL
        // 2 - Cache Image
        // 3 - Load image into ImageView
        listOfNews[position].userUID?.let { checkedUserUID ->
            FirebaseFirestore.getInstance()
                .collection("Users")
                .document(checkedUserUID)
                .get()
                .addOnSuccessListener { documentSnapshot ->

                    val user = documentSnapshot.toObject(UserModel::class.java)
                    if (user != null) {
                        Glide.with(holder.imageUserView.context).load(user.url)
                            .into(holder.imageUserView)
                    }
                }.addOnFailureListener { e ->

                }

        }
        Glide.with(holder.imageUserView.context).load(listOfNews[position])
            .into(holder.imageUserView)
    }


     fun GetNews(position: Int): NewsModel {
        return listOfNews.get(position)
    }

}