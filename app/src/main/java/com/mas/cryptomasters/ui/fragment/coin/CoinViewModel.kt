package com.mas.cryptomasters.ui.fragment.coin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mas.cryptomasters.core.api.api2.ApiRepository2
import com.mas.cryptomasters.data.Response
import com.mas.cryptomasters.data.models.coin.Coins
import com.mas.cryptomasters.utils.Extensions.isRequestSuccess
import com.mas.cryptomasters.utils.RESPONSE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CoinViewModel @Inject constructor(val apiRepository: ApiRepository2) : ViewModel() {

    private val _coins = MutableLiveData<Response<List<Coins>>>()
    val coins: LiveData<Response<List<Coins>>> = _coins

    private var currentPage = 1

    fun initPagination() {
        currentPage = 1
        getCoins(currentPage)
    }

    private fun getCoins(currentPage:Int) {
        viewModelScope.launch {
            apiRepository.getCoins(currentPage).let {
                when (it.isRequestSuccess(it.code())) {
                    RESPONSE.AUT -> _coins.postValue(Response(reLogin = true))
                    RESPONSE.ERROR -> _coins.postValue(Response(error = it.toString()))
                    RESPONSE.SUCCESS -> _coins.postValue(Response(data = it.body(), flag = 1))
                }
            }
        }
    }

    fun fetchNextPage() {
        currentPage++
        getCoins(currentPage)
    }

}
//
//@HiltViewModel
//class CoinViewModel @Inject constructor(val apiRepository: ApiRepository2) : ViewModel() {
//    val coinMutable: MutableLiveData<Response<Any?>> = MutableLiveData()
//    private var currentPage = 1
//    init {
//        getCoinContent(currentPage)
//    }
//
//
//    private fun getCoinContent(currentPage: Int) {
//        viewModelScope.launch {
//            apiRepository.getCoins(currentPage).let {
//                when (it.isRequestSuccess(it.code())) {
//                    RESPONSE.AUT -> coinMutable.postValue(Response(reLogin = true))
//                    RESPONSE.ERROR -> coinMutable.postValue(Response(error = it.toString()))
//                    RESPONSE.SUCCESS -> coinMutable.postValue(
//                        Response(
//                            data =
//                            it.body(),
//                            flag = 1
//                        )
//                    )
//                }
//            }
//        }
//    }
//    fun fetchNextPage() {
//        currentPage++
//        getCoinContent(currentPage)
//    }
//}