package com.mas.cryptomasters.data.request
import com.google.gson.annotations.SerializedName


data class ContactRequest(
    @SerializedName("name")
    val name: String,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("subject")
    val subject: String,
    @SerializedName("text")
    val text: String
)