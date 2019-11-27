package com.example.myapplicationaaa1.fragment

import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplicationaaa1.R
import com.example.myapplicationaaa1.model.NewsList
import com.example.myapplicationaaa1.model.NewsModel
import com.example.myapplicationaaa1.utils.NewsAdapter
import com.example.myapplicationaaa1.utils.UserDao
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_news.*

class News_Fragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_news)

        // READ DB AND STORE THE INFO IN NEWSLIST
        lateinit var  newsListModel: NewsList

        UserDao().getAllPosts(
            successListener = {
//                newsListModel=it
                // Get List of news
                val news: ArrayList<NewsModel>? = newsListModel.news
                // Configure Recyclerview
                recyclerView.adapter = NewsAdapter(ArrayList(news.orEmpty()))
                recyclerView.layoutManager = LinearLayoutManager(context)
            },
            failureListener={
                Toast.makeText(context, "No se han encontrado posts pruebalo de nuevo mas tarde", Toast.LENGTH_LONG).show()
            }
        )


    }






}

