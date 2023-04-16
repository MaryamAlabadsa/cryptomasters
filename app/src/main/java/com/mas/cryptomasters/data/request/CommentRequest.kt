package com.mas.cryptomasters.data.request
import com.google.gson.annotations.SerializedName

data class CommentRequest(
    @SerializedName("comment")
    val comment: String,
    @SerializedName("post_id")
    val postId: Int
)