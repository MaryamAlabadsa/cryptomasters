package com.mas.cryptomasters.data.response.home
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Coin(
    @SerializedName("body")
    val body: String,
    @SerializedName("halal")
    val halal: String,
    @SerializedName("icon")
    val icon: String?,
    @SerializedName("id")
    val id: Int,
    @SerializedName("image")
    val image: String?,
    @SerializedName("isHalal")
    val isHalal: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("project")
    val project: String
) : Parcelable