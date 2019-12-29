package com.example.myapplicationaaa1.model


data class TWUserModel(
    var display_name: String? = null,
    var email: String? = null,
    var login: String? = null,
    var description: String? = null,
    private var profile_image_url: String? = null
) {
    val imageUrl: String?
        get() {
            return profile_image_url?.replace("{width}x{height}", "500x500")
        }
}

data class TWUserResponse(
    var data: ArrayList<TWUserModel>? = null
)