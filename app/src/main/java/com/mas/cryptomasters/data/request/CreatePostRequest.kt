package com.mas.cryptomasters.data.request

import com.google.gson.annotations.SerializedName
import okhttp3.RequestBody

data class CreatePostRequest(
    @SerializedName("content")
    var content: String = "",
    @SerializedName("images")
    var images: List<String>? = null,
    @SerializedName("title")
    var title: String = "",
    @SerializedName("video")
    var video: String = "",
)
