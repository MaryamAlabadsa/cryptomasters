package com.mas.cryptomasters.data.request
import com.google.gson.annotations.SerializedName


data class EditProfileRequest(
    @SerializedName("avatar")
    val avatar: String,
    @SerializedName("name")
    val name: String
)