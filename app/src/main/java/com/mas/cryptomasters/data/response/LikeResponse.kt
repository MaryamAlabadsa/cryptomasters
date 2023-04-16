package com.mas.cryptomasters.data.response

import com.google.gson.annotations.SerializedName
import com.mas.cryptomasters.data.response.home.Like


data class LikeResponse(
    @SerializedName("data")
    var like: Like,
) : BaseResponse()

