package com.mas.cryptomasters.core.api.api2

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
import javax.inject.Inject

class ApiHelperImplement2 @Inject constructor(private val apiService: ApiService2) : ApiHelper2{

    override suspend fun getCoins(): Response<List<Coins>> = apiService.getCoins()
}