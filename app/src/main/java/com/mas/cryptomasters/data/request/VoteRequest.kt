package com.mas.cryptomasters.data.request

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class VoteRequest(
    @SerializedName("recommendation_id")
    val recommendation_id: Int,
    @SerializedName("vote")
    val vote: Int
) : Parcelable