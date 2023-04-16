package com.mas.cryptomasters.data.response


import com.google.gson.annotations.SerializedName

data class SettingsResponse(
    @SerializedName("data")
    val settingsObject: SettingsObject? = SettingsObject(),
) : BaseResponse()

data class SettingsObject(
    @SerializedName("android")
    val android: String? = "",
    @SerializedName("app_about")
    val appAbout: String? = "",
    @SerializedName("app_privacy")
    val appPrivacy: String? = "",
    @SerializedName("app_terms")
    val appTerms: String? = "",
    @SerializedName("fb")
    val fb: String? = "",
    @SerializedName("id")
    val id: Int? = 0,
    @SerializedName("ig")
    val ig: String? = "",
    @SerializedName("ios")
    val ios: String? = "",
    @SerializedName("sn")
    val sn: String? = "",
    @SerializedName("tg")
    val tg: String? = null,
    @SerializedName("tw")
    val tw: String? = "",
    @SerializedName("whts")
    val whts: String? = null,
    @SerializedName("yt")
    val yt: String? = "",
    @SerializedName("isHide")
    val isHide: String? = ""
)