package com.mas.cryptomasters.data.response


import com.google.gson.annotations.SerializedName
import com.mas.cryptomasters.data.models.coin.Coins
import com.mas.cryptomasters.data.response.home.Coin
import kotlinx.parcelize.Parcelize

//@Parcelize
//data class SearchResponse(
//    @SerializedName("code")
//    val code: Int,
//    @SerializedName("message")
//    val message: String,
//    @SerializedName("success")
//    val success: String,
//    @SerializedName("data")
//    val coins: List<Coin>,
//) : BaseResponse()
data class SearchResponse (

    @SerializedName("code"    ) var code    : Int?            = null,
    @SerializedName("message" ) var message : String?         = null,
    @SerializedName("success" ) var success : String?         = null,
    @SerializedName("data"    ) var coins    : ArrayList<Coin> = arrayListOf()

)