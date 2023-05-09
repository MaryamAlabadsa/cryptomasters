package com.mas.cryptomasters.data.request

import com.google.gson.annotations.SerializedName
import okhttp3.RequestBody

data class SearchRequest(
    @SerializedName("q")
    val q: String
)
