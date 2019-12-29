package com.example.myapplicationaaa1.fragment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplicationaaa1.R
import com.example.myapplicationaaa1.activity.AddPostActivity
import com.example.myapplicationaaa1.activity.PostDetailActivity
import com.example.myapplicationaaa1.adapters.NewsAdapter
import com.example.myapplicationaaa1.model.NewsList
import com.example.myapplicationaaa1.model.NewsModel
import com.example.myapplicationaaa1.utils.ItemClickSupport
import com.example.myapplicationaaa1.utils.UserDao
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_news.*


class News_Fragment : Fragment() {

    var news: ArrayList<NewsModel>? = null
    lateinit var mAdView: AdView

    var runnable: Runnable = Runnable { metodo_timer() }
    var contador = 0
    var handler = Handler()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        runnable.run();
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.fragment_news)

        //ADD
        mAdView = adView
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)
        //******************


        SeePosts()

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
            refreshButton.visibility = GONE

            SeePosts()
        }

        refreshButton.visibility = GONE
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

        SeePosts()

    }

    fun metodo_timer() {

        contador++;
        if (contador > 5) {
            contador = 0
            UserDao().getAllPosts(
                successListener = {
                    var newsListModel = NewsList(it)
                    // Get List of news
                    val news2: ArrayList<NewsModel>? = newsListModel.news



                    if (news!!.size!! < news2!!.size!!) {
                        if(refreshButton.visibility== GONE)
                            refreshButton.visibility = VISIBLE

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

    // Configure item click on RecyclerView
    private fun configureOnClickRecyclerView() {

        val adapter: NewsAdapter = recyclerView.adapter as NewsAdapter
        val nullableIndexLayout: Int? = R.layout.item_news as Int? // allowed, always works

        if (nullableIndexLayout != null) {
            ItemClickSupport.addTo(recyclerView, nullableIndexLayout)
                .setOnItemClickListener(object : ItemClickSupport.OnItemClickListener {
                    override fun onItemClicked(recyclerView: RecyclerView, position: Int, v: View) {
                        // 1 - Get user from adapter
                        val news = adapter.GetNews(position)
                        // 2 - Show result in a Toast
                        Toast.makeText(
                            context,
                            "You clicked on user : " + news.textPosted,
                            Toast.LENGTH_SHORT
                        ).show()

                        //open detail activity
                        val intent = Intent(requireContext(), PostDetailActivity::class.java)
                        intent.putExtra("postUserUID", news.userUID) //User id
                        intent.putExtra("postUID", news.postUID) //Post id
                        startActivity(intent)

                    }
                })
        }

    }

    private fun SeePosts() {
        val progressBar: ProgressBar = progressBar2

        UserDao().getAllPosts(
            successListener = {

                if (progressBar != null)
                    progressBar.visibility = View.GONE

                var newsListModel: NewsList = NewsList(it)
                // Get List of news
                news = newsListModel.news

                // Configure Recyclerview
                if(recyclerView!=null) {
                    recyclerView.adapter =
                        NewsAdapter(ArrayList(news.orEmpty()), activity!!.supportFragmentManager)
                    recyclerView.layoutManager = LinearLayoutManager(context)
                    configureOnClickRecyclerView()
                }

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


}



