package com.mas.cryptomasters.data.request

import com.google.gson.annotations.SerializedName


data class RegisterRequest(
    @SerializedName("avatar")
    val avatar: String="",
    @SerializedName("device_token")
    val deviceToken: String="",
    @SerializedName("name")
    val name: String="",
    @SerializedName("password")
    val password: String="",
    @SerializedName("phone")
    val phone: String=""
)