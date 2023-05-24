package com.mas.cryptomasters.data.response


import com.google.gson.annotations.SerializedName

data class SettingsResponse(
    @SerializedName("data")
    var settingsObject: SettingsObject? = SettingsObject(),
) : BaseResponse()

data class SettingsObject(

    @SerializedName("id"          ) var id         : Int?    = null,
    @SerializedName("app_about"   ) var appAbout   : String? = null,
    @SerializedName("app_privacy" ) var appPrivacy : String? = null,
    @SerializedName("app_terms"   ) var appTerms   : String? = null,
    @SerializedName("android"     ) var android    : String? = null,
    @SerializedName("ios"         ) var ios        : String? = null,
    @SerializedName("fb"          ) var fb         : String? = null,
    @SerializedName("ig"          ) var ig         : String? = null,
    @SerializedName("tg"          ) var tg         : String? = null,
    @SerializedName("tw"          ) var tw         : String? = null,
    @SerializedName("yt"          ) var yt         : String? = null,
    @SerializedName("sn"          ) var sn         : String? = null,
    @SerializedName("whts"        ) var whts       : String? = null,
    @SerializedName("isHide"      ) var isHide     : String? = null

)