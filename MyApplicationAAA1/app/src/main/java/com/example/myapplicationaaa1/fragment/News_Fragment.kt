package com.example.myapplicationaaa1.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplicationaaa1.R
import com.example.myapplicationaaa1.activity.AddPostActivity
import com.example.myapplicationaaa1.activity.RegisterActivity
import com.example.myapplicationaaa1.adapters.NewsAdapter
import com.example.myapplicationaaa1.model.NewsList
import com.example.myapplicationaaa1.model.NewsModel
import com.example.myapplicationaaa1.utils.UserDao
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_news.*
import android.os.Handler
import android.view.View.GONE
import android.view.View.VISIBLE


class News_Fragment : Fragment() {

    var news: ArrayList<NewsModel>? = null
    lateinit var mAdView: AdView

    var contador = 0
    var handler = Handler()
    var runnable: Runnable = Runnable { metodo_timer() }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        runnable.run();
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.fragment_news)

        //ADD
        mAdView = adView
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)
        //******************

        UserDao().getAllPosts(
            successListener = {
                val progressBar: ProgressBar = progressBar2

                if (progressBar != null)
                    progressBar.visibility = View.GONE

                var newsListModel: NewsList = NewsList(it)
                // Get List of news
                val news: ArrayList<NewsModel>? = newsListModel.news
                // Configure Recyclerview
                recyclerView.adapter =
                    NewsAdapter(ArrayList(news.orEmpty()))
                recyclerView.layoutManager = LinearLayoutManager(context)
            },
            failureListener = {
                Toast.makeText(
                    context,
                    "No se han encontrado posts pruebalo de nuevo mas tarde",
                    Toast.LENGTH_LONG
                ).show()
            }
        )
        AddPostButton.setOnClickListener {
            if (FirebaseAuth.getInstance().currentUser !== null)
                startActivity(Intent(requireContext(), AddPostActivity::class.java))
            else
                Toast.makeText(
                    context,
                    "Inicie sesion antes para acceder a esta función",
                    Toast.LENGTH_LONG
                ).show()
        }
        refreshButton.setOnClickListener {
            refreshButton.visibility=GONE

            UserDao().getAllPosts(
                successListener = {
                    val progressBar: ProgressBar = progressBar2
                    val newsListModel: NewsList = NewsList(it)
                    // Get List of news
                    news = newsListModel.news
                    // Configure Recyclerview
                    recyclerView.adapter =
                        NewsAdapter(ArrayList(news.orEmpty()))
                    recyclerView.layoutManager = LinearLayoutManager(context)
                    Toast.makeText(
                        context,
                        "Posts actualizados",
                        Toast.LENGTH_LONG
                    ).show()
                },
                failureListener = {
                    Toast.makeText(
                        context,
                        "No se ha podido refrescar los posts",
                        Toast.LENGTH_LONG
                    ).show()
                }
            )
        }

        refreshButton.visibility=GONE
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater!!.inflate(R.layout.fragment_news, container, false)

        return view;
    }

    override fun onResume() {
        super.onResume()
        UserDao().getAllPosts(
            successListener = {
                val progressBar: ProgressBar = progressBar2


                val newsListModel: NewsList = NewsList(it)
                // Get List of news
                news = newsListModel.news
                // Configure Recyclerview
                recyclerView.adapter =
                    NewsAdapter(ArrayList(news.orEmpty()))
                recyclerView.layoutManager = LinearLayoutManager(context)
            },
            failureListener = {
                Toast.makeText(
                    context,
                    "No se han encontrado posts pruebalo de nuevo mas tarde",
                    Toast.LENGTH_LONG
                ).show()
            }
        )

    }

    fun  metodo_timer(){

        contador ++;
        if (contador>5) {
            contador = 0
            UserDao().getAllPosts(
                successListener = {
                    var newsListModel: NewsList = NewsList(it)
                    // Get List of news
                    val news2: ArrayList<NewsModel>? = newsListModel.news

                    if (news?.size!! < news2?.size!!){
                        refreshButton.visibility= VISIBLE

                    }
                },
                failureListener = {
                    Toast.makeText(
                        context,
                        "No se han detectado posts al mirar",
                        Toast.LENGTH_LONG
                    ).show()
                }
            )


        }


        handler.postDelayed(runnable, 1000);
    }



}



