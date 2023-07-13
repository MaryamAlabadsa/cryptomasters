package com.mas.cryptomasters.data.response.recommendations


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.mas.cryptomasters.data.response.home.User
import kotlinx.parcelize.Parcelize

@Parcelize
data class RecommendData(

    @SerializedName("id") var id: Int? = null,
    @SerializedName("type") var type: String? = null,
    @SerializedName("title") var title: String? = null,
    @SerializedName("content") var content: String? = null,
    @SerializedName("created_at") var createdAt: String? = null,
    @SerializedName("isVoted") var isVoted: Int? = null,
    @SerializedName("vote_type") var voteType: String? = null,
    @SerializedName("votes_count") var votesCount: Int? = null,
    @SerializedName("up_count") var upCount: Int? = null,
    @SerializedName("down_count") var downCount: Int? = null,
    @SerializedName("votes") var votes: ArrayList<Votes> = arrayListOf()
) : Parcelable

@Parcelize
data class Votes(

    @SerializedName("id") var id: Int? = null,
    @SerializedName("recommendation_id") var recommendationId: String? = null,
    @SerializedName("user_id") var userId: String? = null,
    @SerializedName("vote") var vote: String? = null,
    @SerializedName("created_at") var createdAt: String? = null,
    @SerializedName("updated_at") var updatedAt: String? = null,
    @SerializedName("vote_type") var voteType: String? = null,
    @SerializedName("user") var user: User? = null

) : Parcelable

@Parcelize
data class RecomandedUser(

    @SerializedName("id") var id: Int? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("phone") var phone: String? = null,
    @SerializedName("email") var email: String? = null,
    @SerializedName("registerBy") var registerBy: String? = null,
    @SerializedName("api_token") var apiToken: String? = null,
    @SerializedName("avatar") var avatar: String? = null,
    @SerializedName("device_token") var deviceToken: String? = null,
    @SerializedName("isCompleted") var isCompleted: String? = null,
    @SerializedName("isActive") var isActive: String? = null,
    @SerializedName("isPaid") var isPaid: String? = null,
    @SerializedName("subscribe_end") var subscribeEnd: String? = null,
    @SerializedName("created_at") var createdAt: String? = null,
    @SerializedName("updated_at") var updatedAt: String? = null

) : Parcelable