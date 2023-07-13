package com.mas.cryptomasters.data.response.home


import com.google.gson.annotations.SerializedName
data class Data(
    @SerializedName("coins")
    val coins: List<Coin>,
    @SerializedName("posts")
    val posts: List<PostsData>,
    @SerializedName("recommendations")
    val recommendations: List<Recommendation>
)