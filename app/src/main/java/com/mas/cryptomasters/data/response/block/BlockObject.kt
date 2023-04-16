package com.mas.cryptomasters.data.response.block


import com.google.gson.annotations.SerializedName

data class BlockObject(
    @SerializedName("api_token")
    val apiToken: String,
    @SerializedName("avatar")
    val avatar: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("device_token")
    val deviceToken: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("isActive")
    val isActive: Int,
    @SerializedName("isCompleted")
    val isCompleted: Int,
    @SerializedName("isPaid")
    val isPaid: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("subscribe_end")
    val subscribeEnd: Any,
    @SerializedName("updated_at")
    val updatedAt: String
)