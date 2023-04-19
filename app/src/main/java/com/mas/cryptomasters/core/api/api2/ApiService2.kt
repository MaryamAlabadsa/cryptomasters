package com.mas.cryptomasters.core.api.api2

import com.mas.cryptomasters.data.models.ChartResponse
import com.mas.cryptomasters.data.models.coin.Coins
import com.mas.cryptomasters.data.request.*
import com.mas.cryptomasters.data.response.*
import com.mas.cryptomasters.data.response.block.BlockResponse
import com.mas.cryptomasters.data.response.home.HomeResponse
import com.mas.cryptomasters.data.response.notifications.NotificationsResponse
import com.mas.cryptomasters.data.response.recommendations.Recommendations
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*


interface ApiService2 {
    @GET("coins/markets")
    suspend fun getCoins(
        @Query("vs_currency") currency: String = "usd",
        @Query("order") order: String = "market_cap_desc",
        @Query("per_page") perPage: Int = 30,
        @Query("page") page: Int =1,
        @Query("sparkline") sparkline: Boolean = false
    ): Response<List<Coins>>

    @GET("coins/{id}/market_chart")
    suspend fun getMarketChart(
        @Path("id") coinId: String = "bitcoin",
        @Query("vs_currency") currency: String = "usd",
        @Query("days") days: Int = 1
    ): Response<ChartResponse>

}