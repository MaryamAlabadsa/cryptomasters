package com.mas.cryptomasters.data.request

import com.google.gson.annotations.SerializedName


data class UpdatePostRequest(
    @SerializedName("content")
    var content: String? = "",
    @SerializedName("title")
    var title: String? = "",
    @SerializedName("video")
    var video: String? = "",
    @SerializedName("image")
    var image: String? = ""
)