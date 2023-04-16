package com.mas.cryptomasters.data.request
import com.google.gson.annotations.SerializedName


data class ResetPasswordRequest(
    @SerializedName("password")
    val password: String ="",
    @SerializedName("password_confirmation")
    val passwordConfirmation: String="",
    @SerializedName("phone")
    var phone: String? = null
)