package com.mas.cryptomasters.data.response.home


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    @SerializedName("api_token")
    val apiToken: String?,
    @SerializedName("avatar")
    val avatar: String?,
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("device_token")
    val deviceToken: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("isActive")
    val isActive: String?,
    @SerializedName("isCompleted")
    val isCompleted: String?,
    @SerializedName("isPaid")
    val isPaid: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("phone")
    val phone: String?,
    @SerializedName("updated_at")
    val updatedAt: String?
) : Parcelable