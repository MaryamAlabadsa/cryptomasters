package com.mas.cryptomasters.data.request


import com.google.gson.annotations.SerializedName

data class VisitorRequest(
    @SerializedName("device_token")
    val deviceToken: String
)