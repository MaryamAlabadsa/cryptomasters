package com.mas.cryptomasters.data.request

import com.google.gson.annotations.SerializedName
import okhttp3.Headers
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

data class UploadVideoRequest(
    @SerializedName("video")
    val video: RequestBody? = null
)