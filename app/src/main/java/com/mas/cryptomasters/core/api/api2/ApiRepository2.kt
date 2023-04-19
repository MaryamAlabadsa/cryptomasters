package com.mas.cryptomasters.core.api.api2

import com.mas.cryptomasters.data.models.ChartResponse
import com.mas.cryptomasters.data.models.coin.Coins
import retrofit2.Response
import javax.inject.Inject

class ApiRepository2 @Inject constructor(private val apiService: ApiService2) {
    suspend fun getCoins(currentPage: Int): Response<List<Coins>> = apiService.getCoins("usd",
        "market_cap_desc",30,currentPage)
    suspend fun getMarketChart(id: String, s: String, days: Int): Response<ChartResponse> = apiService.getMarketChart(id,s,days)
}