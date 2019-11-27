package com.example.myapplicationaaa1.model


data class NewsList(
    var news:ArrayList<NewsModel>?
)

data class NewsModel(

    var textPosted: String?,
    val imageUserUrl: String? = null,
    val userName: String?=null

    )

