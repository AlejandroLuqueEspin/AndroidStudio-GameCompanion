package com.example.myapplicationaaa1.model

import com.google.gson.annotations.SerializedName

data class StreamsModel (
    val id:String?=null,
    @SerializedName ("user_id")val userId:String?=null,
    @SerializedName ("user_name")val userName:String?=null,
    @SerializedName ("user_id")val gameId:String?=null,
    val title:String?=null,
    @SerializedName ("started_at")val startedAt:String?=null,
    @SerializedName ("thumbnail_url")val thumbnailUrl:String?=null

){
    fun getSnailThumbnailUrl():String?{
      return thumbnailUrl?.replace("{width}","300")?.replace("height","300")
    }
}
data class StreamsResponse (
    @SerializedName ("data")val results:List<StreamsModel>?=null


    )