package com.example.myapplicationaaa1.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplicationaaa1.R
import com.example.myapplicationaaa1.adapters.NewsAdapter
import com.example.myapplicationaaa1.model.NewsList
import com.example.myapplicationaaa1.model.NewsModel
import com.example.myapplicationaaa1.model.UserModel
import com.example.myapplicationaaa1.utils.UserDao
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import kotlinx.android.synthetic.main.activity_post_detail.*


class PostDetailActivity : AppCompatActivity() {

    var userPostUID: String? = null
    var postUID: String? = null

    var user:UserModel?=null
    var post:NewsModel?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val b = getIntent().getExtras()
        if (b != null) {
            userPostUID = b.getString("postUserUID")
            postUID = b.getString("postUID")
        } else
            finish()

        setContentView(R.layout.activity_post_detail)
        GetInfo()


        addCommentButton.setOnClickListener {
            if (FirebaseAuth.getInstance().currentUser !== null&&postUID!==null){

                val intent = Intent(this, AddCommentActivity::class.java)
                intent.putExtra("postUID", postUID) //Post id
                startActivity(intent)
            }
            else
                Toast.makeText(
                    this,
                    "Inicie sesion antes para acceder a esta función",
                    Toast.LENGTH_LONG
                ).show()
        }
    }

    override fun onResume() {
        super.onResume()
        SeePosts()
    }

    private fun GetInfo() {

        val dataBase = FirebaseFirestore.getInstance()

        //GetUserWhoPost
        userPostUID?.let { checkedUserUID ->
            dataBase
                .collection("Users")
                .document(checkedUserUID)
                .get()
                .addOnSuccessListener { documentSnapshot ->

                    user = documentSnapshot.toObject(UserModel::class.java)
                    UpdateUI()

                }.addOnFailureListener { e ->
                    Toast.makeText(
                        this,
                        "Error, to acces the user",
                        Toast.LENGTH_LONG
                    ).show()

                }

        }
        //GetThePost
        userPostUID?.let { checkedUserUID ->
            postUID?.let { checkedPostUID ->
                dataBase
                    .collection("Users")
                    .document(checkedUserUID)
                    .collection("posts")
                    .document(checkedPostUID)
                    .get()
                    .addOnSuccessListener { documentSnapshot ->

                        post = documentSnapshot.toObject(NewsModel::class.java)
                        UpdateUI()

                    }.addOnFailureListener { e ->
                        Toast.makeText(
                            this,
                            "Error, to acces the post",
                            Toast.LENGTH_LONG
                        ).show()

                    }
            }
        }
    }

    private fun UpdateUI(){

        userNameViewCommentDetail.text= user?.userName
        Glide.with(userImageViewCommentDetail.context).load(user?.url).into(userImageViewCommentDetail)

        textPostedCommentDetail.text=post?.textPosted

        SeePosts()



    }
    private fun SeePosts() {

        postUID?.let {checkedPostUID->
            UserDao().getAllComments(postUID = checkedPostUID,
                successListener = {
                    //val progressBar: ProgressBar = progressBar2

    //                if (progressBar != null)
    //                    progressBar.visibility = View.GONE

                    var newsListModel: NewsList = NewsList(it)
                    // Get List of news
                    val news: ArrayList<NewsModel>? = newsListModel.news
                    // Configure Recyclerview
                    recyclerViewPostDetailComments.adapter =
                        NewsAdapter(ArrayList(news.orEmpty()),this!!.supportFragmentManager)
                    recyclerViewPostDetailComments.layoutManager = LinearLayoutManager(applicationContext)

                },
                failureListener = {
                    Toast.makeText(
                        applicationContext,
                        "No se han encontrado posts pruebalo de nuevo mas tarde",
                        Toast.LENGTH_LONG
                    ).show()
                }
            )
        }
    }
}
