package com.mas.cryptomasters.data.response
import com.google.gson.annotations.SerializedName


data class EditCommentRequest(
    @SerializedName("comment")
    val comment: String
)