package com.mas.cryptomasters.data.request

import com.google.gson.annotations.SerializedName

data class DeleteImagesRequest(
    @SerializedName("images")
    val images: List<Int>
)