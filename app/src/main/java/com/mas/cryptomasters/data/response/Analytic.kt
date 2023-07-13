package com.mas.cryptomasters.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.mas.cryptomasters.data.response.recommendations.Links
import com.mas.cryptomasters.data.response.recommendations.RecommendData
import kotlinx.parcelize.Parcelize



@Parcelize
data class Analytic(
    @SerializedName("data") var analyticData: ArrayList<AnalyticData> = arrayListOf(),
) : BaseResponse()

@Parcelize
data class AnalyticData(

    @SerializedName("id") var id: Int? = null,
    @SerializedName("title") var title: String? = null,
    @SerializedName("content") var content: String? = null,
    @SerializedName("created_at") var createdAt: String? = null,

) : Parcelable