package com.mas.cryptomasters.core.api.api2

import com.mas.cryptomasters.data.models.ChartResponse
import com.mas.cryptomasters.data.models.coin.Coins
import retrofit2.Response
import javax.inject.Inject

class ApiHelperImplement2 @Inject constructor(private val apiService: ApiService2) : ApiHelper2{

    override suspend fun getCoins(): Response<List<Coins>> = apiService.getCoins()
    override suspend fun getMarketChart(): Response<ChartResponse> = apiService.getMarketChart()
}