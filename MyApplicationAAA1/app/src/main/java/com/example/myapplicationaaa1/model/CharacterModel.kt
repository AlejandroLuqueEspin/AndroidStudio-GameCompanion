package com.example.myapplicationaaa1.model

import android.webkit.WebStorage
import com.google.gson.annotations.SerializedName

data class CharacterModel(


    var id: Int?=null,
    var name: String?=null,
    @SerializedName("status") val lifeStatus: String? = null,
    @SerializedName("origin")val characterOrigin: CharacterOrigin?=null,
    @SerializedName("episode")val episode: List<String>?=null,
    val created: String?=null

)
data class CharacterOrigin(
    var name: String?=null,
    var url: String?=null
)

data class CharacterResponse(
    var results: List<CharacterModel>?=null
)

