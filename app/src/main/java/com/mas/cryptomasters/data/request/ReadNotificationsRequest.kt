package com.mas.cryptomasters.data.request
import com.google.gson.annotations.SerializedName


data class ReadNotificationsRequest(
    @SerializedName("isRead")
    val isRead: Int,
    @SerializedName("notification_id")
    val notificationId: Int
)