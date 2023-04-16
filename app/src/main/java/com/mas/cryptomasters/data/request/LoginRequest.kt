package com.mas.cryptomasters.data.request
import com.google.gson.annotations.SerializedName



data class LoginRequest(
    @SerializedName("device_token")
    val deviceToken: String="",
    @SerializedName("password")
    val password: String="",
    @SerializedName("phone")
    val phone: String=""
)