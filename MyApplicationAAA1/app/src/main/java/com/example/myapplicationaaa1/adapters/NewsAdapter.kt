package com.example.myapplicationaaa1.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplicationaaa1.R
import com.example.myapplicationaaa1.model.NewsModel
import kotlinx.android.synthetic.main.item_news.view.*

/**
 * Created by alex on 2019-10-11.
 */

class NewsAdapter(var listOfNews: ArrayList<NewsModel>) : RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

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
        Glide.with(holder.imageUserView.context).load(listOfNews[position].imageUserUrl)
            .into(holder.imageUserView)
    }


}