package com.mas.cryptomasters.data.response


import com.google.gson.annotations.SerializedName
import com.mas.cryptomasters.data.models.coin.Coins
import com.mas.cryptomasters.data.response.home.Coin
import kotlinx.parcelize.Parcelize

@Parcelize
data class CoinsResponse(
    @SerializedName("data")
    val coins: List<Coin>,
) : BaseResponse()