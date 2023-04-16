package com.mas.cryptomasters.data.request
import com.google.gson.annotations.SerializedName


data class LikePostRequest(
    @SerializedName("post_id")
    val postId: Int
)