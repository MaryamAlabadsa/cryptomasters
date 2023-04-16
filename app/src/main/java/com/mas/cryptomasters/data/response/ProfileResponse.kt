package com.mas.cryptomasters.data.response


import com.google.gson.annotations.SerializedName
import com.mas.cryptomasters.data.response.home.PostsData

data class ProfileResponse(
    @SerializedName("data")
    val profileObject: ProfileObject? = ProfileObject(),
) : BaseResponse()

data class ProfileObject(
    @SerializedName("api_token")
    val apiToken: String? = "",
    @SerializedName("avatar")
    val avatar: String? = "",
    @SerializedName("created_at")
    val createdAt: String? = "",
    @SerializedName("device_token")
    val deviceToken: String? = "",
    @SerializedName("id")
    val id: Int? = 0,
    @SerializedName("isActive")
    val isActive: String? = "",
    @SerializedName("isCompleted")
    val isCompleted: String? = "",
    @SerializedName("isPaid")
    val isPaid: String? = "",
    @SerializedName("name")
    val name: String? = "",
    @SerializedName("phone")
    val phone: String? = "",
    @SerializedName("updated_at")
    val updatedAt: String? = "",
    @SerializedName("posts")
    var posts: ArrayList<PostsData>? = null
)