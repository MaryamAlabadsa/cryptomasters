package com.mas.cryptomasters.ui.fragment.rules

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mas.cryptomasters.core.api.ApiRepository
import com.mas.cryptomasters.data.Response
import com.mas.cryptomasters.utils.Extensions.isRequestSuccess
import com.mas.cryptomasters.utils.RESPONSE
import com.mas.cryptomasters.utils.RESPONSE.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RuleViewModel @Inject constructor(apiRepository: ApiRepository) : ViewModel() {
    var mutableRules = MutableLiveData<Response<Any?>>()

    init {
        viewModelScope.launch {
            apiRepository.getCoins()
                .let {
                    when (it.isRequestSuccess(it.body()!!.code)) {
                        AUT -> mutableRules.postValue(Response(reLogin = true))
                        ERROR -> mutableRules.postValue(Response(error = it.toString()))
                        SUCCESS -> mutableRules.postValue(Response(data = it.body()))
                    }
                }
        }
    }
}