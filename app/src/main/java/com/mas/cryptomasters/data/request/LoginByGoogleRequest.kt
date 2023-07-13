package com.mas.cryptomasters.data.request

import android.net.Uri
import com.google.gson.annotations.SerializedName


data class LoginByGoogleRequest(
    @SerializedName("device_token")
    val deviceToken: String = "",
    @SerializedName("name")
    val name: String = "",
    @SerializedName("email")
    val email: String = "",
    @SerializedName("registerBy")
    val registerBy: String = "",
    @SerializedName("avatar")
    val avatar: String? = ""
)