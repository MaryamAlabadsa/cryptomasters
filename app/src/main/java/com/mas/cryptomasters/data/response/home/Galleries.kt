package com.mas.cryptomasters.data.response.home


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Galleries(
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("image")
    val image: String,
    @SerializedName("post_id")
    val postId: String,
    @SerializedName("updated_at")
    val updatedAt: String
) : Parcelable