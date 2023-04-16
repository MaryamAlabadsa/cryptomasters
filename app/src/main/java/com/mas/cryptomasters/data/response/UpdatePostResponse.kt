package com.mas.cryptomasters.data.response

import com.google.gson.annotations.SerializedName


data class UpdatePostResponse(
    @SerializedName("data")
    val data: UpdatePostObject,
) : BaseResponse()

data class UpdatePostObject(
    @SerializedName("content")
    val content: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("status")
    val status: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("user_id")
    val userId: String,
    @SerializedName("video")
    val video: Any
)