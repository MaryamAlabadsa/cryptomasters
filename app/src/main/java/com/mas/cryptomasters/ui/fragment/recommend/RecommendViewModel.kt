package com.mas.cryptomasters.ui.fragment.recommend

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mas.cryptomasters.core.api.ApiRepository
import com.mas.cryptomasters.data.Response
import com.mas.cryptomasters.data.request.VoteRequest
import com.mas.cryptomasters.utils.Extensions.isRequestSuccess
import com.mas.cryptomasters.utils.RESPONSE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecommendViewModel @Inject constructor(private val apiRepository: ApiRepository) :
    ViewModel() {
    val mutableRecommend = MutableLiveData<Response<Any?>>()

    lateinit var voteRequest: VoteRequest

//    init {
//        getDailyRecommendList()
//    }

    fun getDailyRecommendList(page: Int?) {
        viewModelScope.launch {
            if (page != null) {
                apiRepository.getDailyRecommendations(page)
                    .let {
                        Log.e("error_mart",it.code().toString())
                        when (it.isRequestSuccess(it.code())) {
                            RESPONSE.AUT -> mutableRecommend.postValue(Response(reLogin = true))
                            RESPONSE.ERROR -> mutableRecommend.postValue(Response(error = "${it.errorBody()}"))
                            RESPONSE.SUCCESS -> mutableRecommend.postValue(Response(it.body()!!))
                        }
                    }
            }
        }
    }
    fun getMonthlyRecommendList(page: Int) {
        viewModelScope.launch {
            apiRepository.getMonthlyRecommendations(page)
                .let {
                    when (it.isRequestSuccess(it.code())) {
                        RESPONSE.AUT -> mutableRecommend.postValue(Response(reLogin = true))
                        RESPONSE.ERROR -> mutableRecommend.postValue(Response(error = "${it.errorBody()}"))
                        RESPONSE.SUCCESS -> mutableRecommend.postValue(Response(it.body()!!))

                    }
                }
        }
    }
    fun getWeaklyRecommendList(page: Int) {
        viewModelScope.launch {
            apiRepository.getWeaklyRecommendations(page)
                .let {
                    when (it.isRequestSuccess(it.code())) {
                        RESPONSE.AUT -> mutableRecommend.postValue(Response(reLogin = true))
                        RESPONSE.ERROR -> mutableRecommend.postValue(Response(error = "${it.errorBody()}"))
                        RESPONSE.SUCCESS -> mutableRecommend.postValue(Response(it.body()!!))

                    }
                }
        }
    }

    fun setVote() {
        viewModelScope.launch {
            apiRepository.setVote(voteRequest)
                .let {
                    when (it.isRequestSuccess(it.body()!!.code)) {
                        RESPONSE.AUT -> mutableRecommend.postValue(Response(reLogin = true))
                        RESPONSE.ERROR -> mutableRecommend.postValue(Response(error = "${it.errorBody()}"))
                        RESPONSE.SUCCESS -> mutableRecommend.postValue(Response(data = null))
                    }
                }
        }
    }
}