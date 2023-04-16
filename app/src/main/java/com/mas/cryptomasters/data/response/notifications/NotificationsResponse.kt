package com.mas.cryptomasters.data.response.notifications
import com.google.gson.annotations.SerializedName
import com.mas.cryptomasters.data.response.BaseResponse
import com.mas.cryptomasters.data.response.home.User


data class NotificationsResponse(
    @SerializedName("data")
    val data: List<NotificationsData>,
) : BaseResponse()

data class NotificationsData(
    @SerializedName("action")
    val action: String,
    @SerializedName("body")
    val body: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("post_id")
    val postId: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("user")
    val user: User? = null
)

