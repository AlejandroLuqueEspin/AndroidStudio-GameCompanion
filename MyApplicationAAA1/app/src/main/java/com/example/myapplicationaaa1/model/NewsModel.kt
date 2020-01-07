package com.example.myapplicationaaa1.model


data class NewsList(
    var news: ArrayList<NewsModel>?
)

data class NewsModel(

    var textPosted: String? = null,
    val imageUserUrl: String? = null,
    val userUID: String? = null,
    val postUID: String? = null,
    val upPostUID: String? = null,
    val userName: String? = null

)

