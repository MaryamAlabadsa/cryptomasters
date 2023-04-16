package com.mas.cryptomasters.data.response.home


import com.google.gson.annotations.SerializedName

data class Recommendation(
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
    val isVoted: Any,
    @SerializedName("title")
    val title: String,
    @SerializedName("up_count")
    val upCount: Double,
    @SerializedName("votes")
    val votes: List<Vote>,
    @SerializedName("votes_count")
    val votesCount: Int
)