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
import retrofit2.http.Body
import retrofit2.http.Part
import retrofit2.http.PartMap
import retrofit2.http.Path

interface ApiHelper2 {
    suspend fun getCoins(): Response<List<Coins>>
    suspend fun getMarketChart(): Response<ChartResponse>

}