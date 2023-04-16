package com.mas.cryptomasters.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
open class BaseResponse(
    @SerializedName("code")
    val code: Int? = 0,
    @SerializedName("message")
    val message: String? = "",
    @SerializedName("success")
    val success: String? = ""
) : Parcelable