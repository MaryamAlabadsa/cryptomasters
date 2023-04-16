package com.mas.cryptomasters.data.response.recommendations


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class RecommendData(
    @SerializedName("content")
    val content: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("down_count")
    val downCount: Double,
    @SerializedName("id")
    val id: Int,
    @SerializedName("image")
    val image: String,
    @SerializedName("isVoted")
    var isVoted: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("vote_type")
    var type: String,
    @SerializedName("up_count")
    val upCount: Double,
    @SerializedName("votes")
    val  votes: ArrayList<Vote>,
    @SerializedName("votes_count")
    val votesCount: Int
) : Parcelable