package com.mas.cryptomasters.ui.analytic

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mas.cryptomasters.core.api.ApiRepository
import com.mas.cryptomasters.data.Response
import com.mas.cryptomasters.data.models.coin.Coins
import com.mas.cryptomasters.data.request.VoteRequest
import com.mas.cryptomasters.utils.Extensions.isRequestSuccess
import com.mas.cryptomasters.utils.RESPONSE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnalyticViewModel @Inject constructor(val apiRepository: ApiRepository) : ViewModel() {

    val mutableRecommend = MutableLiveData<Response<Any?>>()

    fun init() {
        getAnalytic()
    }

    private fun getAnalytic() {
        viewModelScope.launch {
            apiRepository.getAnalytic().let {
                when (it.isRequestSuccess(it.code())) {
                    RESPONSE.AUT -> mutableRecommend.postValue(Response(reLogin = true))
                    RESPONSE.ERROR -> mutableRecommend.postValue(Response(error = it.toString()))
                    RESPONSE.SUCCESS -> mutableRecommend.postValue(Response(data = it.body(), flag = 1))
                }
            }
        }
    }

}