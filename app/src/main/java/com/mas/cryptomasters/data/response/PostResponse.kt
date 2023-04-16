package com.mas.cryptomasters.data.response

import com.google.gson.annotations.SerializedName
import com.mas.cryptomasters.data.response.home.PostsData

data class PostResponse(
    @SerializedName("data")
    val data: PostsData,
) : BaseResponse()