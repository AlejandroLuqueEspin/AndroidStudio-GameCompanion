package com.example.myapplicationaaa1.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplicationaaa1.R
import com.example.myapplicationaaa1.model.NewsList
import com.example.myapplicationaaa1.model.NewsModel
import com.example.myapplicationaaa1.utils.NewsAdapter
import com.example.myapplicationaaa1.utils.UserDao
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_news.*
import kotlinx.android.synthetic.main.item_news.*

class News_Fragment : Fragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.fragment_news)

        // READ DB AND STORE THE INFO IN NEWSLIST

        UserDao().getAllPosts(
            successListener = {
                val progressBar:ProgressBar=progressBar2

                if(progressBar!=null)
                    progressBar.visibility=View.GONE

                var  newsListModel: NewsList=NewsList(it)
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater!!.inflate(R.layout.fragment_news, container, false)

        return view;
    }

    private fun UpdateUI(){
        if(FirebaseAuth.getInstance().currentUser==null){
            Toast.makeText(context, "Inicie sesion para ver todo el contenido", Toast.LENGTH_LONG).show()

        }
        else
        {

        }

    }




}

