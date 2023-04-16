package com.mas.cryptomasters.data.response.home


import com.google.gson.annotations.SerializedName
import com.mas.cryptomasters.data.response.BaseResponse

data class HomeResponse(
    @SerializedName("data")
    val `data`: Data,
) : BaseResponse()