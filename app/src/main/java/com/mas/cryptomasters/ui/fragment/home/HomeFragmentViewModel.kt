package com.mas.cryptomasters.ui.fragment.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mas.cryptomasters.core.api.ApiRepository
import com.mas.cryptomasters.data.Response
import com.mas.cryptomasters.utils.Extensions.isRequestSuccess
import com.mas.cryptomasters.utils.RESPONSE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeFragmentViewModel @Inject constructor(val apiRepository: ApiRepository) : ViewModel() {
    val homeMutable: MutableLiveData<Response<Any?>> = MutableLiveData()

    init {
        getHomeContent()
    }


    fun getHomeContent() {
        viewModelScope.launch {
            apiRepository.getHome()
                .let {
                    when (it.isRequestSuccess(it.code())) {
                        RESPONSE.SUCCESS -> homeMutable.postValue(Response(it.body()!!, flag = 0))
                        RESPONSE.ERROR -> homeMutable.postValue(Response(error = "${it.errorBody()}"))
                        RESPONSE.AUT -> homeMutable.postValue(Response(reLogin = true))
                    }
                }
        }
    }


}