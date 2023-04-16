package com.mas.cryptomasters.data.response.recommendations
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.mas.cryptomasters.data.response.home.User
import kotlinx.parcelize.Parcelize

@Parcelize
data class Vote(
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("recommendation_id")
    val recommendationId: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("user")
    val user: User,
    @SerializedName("user_id")
    val userId: String,
    @SerializedName("vote")
    val vote: String,
    @SerializedName("vote_type")
    val voteType: String
) : Parcelable