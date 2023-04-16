package com.mas.cryptomasters.ui.fragment.coin

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mas.cryptomasters.core.api.ApiHelperImplement
import com.mas.cryptomasters.core.api.ApiRepository
import com.mas.cryptomasters.core.api.api2.ApiRepository2
import com.mas.cryptomasters.data.Response
import com.mas.cryptomasters.utils.Extensions.isRequestSuccess
import com.mas.cryptomasters.utils.RESPONSE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CoinViewModel @Inject constructor(val apiRepository: ApiRepository2) : ViewModel() {
    val coinMutable: MutableLiveData<Response<Any?>> = MutableLiveData()

    init {
        getCoinContent()
    }

    private fun getCoinContent() {
        viewModelScope.launch {
            apiRepository.getCoins().let {
                when (it.isRequestSuccess(it.code())) {
                    RESPONSE.AUT -> coinMutable.postValue(Response(reLogin = true))
                    RESPONSE.ERROR -> coinMutable.postValue(Response(error = it.toString()))
                    RESPONSE.SUCCESS -> coinMutable.postValue(
                        Response(
                            data =
                            it.body(),
                            flag = 1
                        )
                    )
                }
            }
        }
    }
}