package com.mas.cryptomasters.data.response

import com.google.gson.annotations.SerializedName


data class DefaultResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val data: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("success")
    val success: String
)