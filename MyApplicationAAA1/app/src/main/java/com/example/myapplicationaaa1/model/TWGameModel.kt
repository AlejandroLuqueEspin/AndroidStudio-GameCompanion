package com.example.myapplicationaaa1.model

data class TWGameModel(
    var name: String? = null,
    var username: String? = null,
    private var box_art_url: String? = null
) {
    val imageUrl: String?
        get() {
            return box_art_url?.replace("{width}x{height}", "500x500")
        }
}

data class TWGameResponse(
    var data: ArrayList<TWGameModel>? = null
)