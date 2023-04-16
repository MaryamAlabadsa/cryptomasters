package com.mas.cryptomasters.data.response.recommendations


import com.google.gson.annotations.SerializedName
import com.mas.cryptomasters.data.response.BaseResponse
import kotlinx.parcelize.Parcelize

@Parcelize
data class Recommendations(
    @SerializedName("data")
    val recommendData: List<RecommendData>,
) : BaseResponse()