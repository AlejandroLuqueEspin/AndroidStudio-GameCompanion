package com.example.myapplicationaaa1.model

import com.google.gson.annotations.SerializedName

data class TWStreamsModel(
    var title: String? = null,
    var user_id: String? = null,
    var game_id: String? = null,
    @SerializedName("user_name")
    var username: String? = null,
    private var thumbnail_url: String? = null
) {
    val imageUrl: String?
        get() {
            return thumbnail_url?.replace("{width}x{height}", "500x350")
        }
}


data class TWStreamsResponse(
    var data: ArrayList<TWStreamsModel>? = null
)
