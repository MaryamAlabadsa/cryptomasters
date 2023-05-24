package com.mas.cryptomasters.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
open class BaseResponse(
    @SerializedName("code"    ) var code    : Int?    = null,
    @SerializedName("message" ) var message : String? = null,
    @SerializedName("success" ) var success : String? = null,
) : Parcelable