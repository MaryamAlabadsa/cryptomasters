package com.mas.cryptomasters.ui.fragment.block

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mas.cryptomasters.core.api.ApiRepository
import com.mas.cryptomasters.data.Response
import com.mas.cryptomasters.data.response.block.BlockData
import com.mas.cryptomasters.utils.Extensions.isRequestSuccess
import com.mas.cryptomasters.utils.RESPONSE.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BlockViewModels @Inject constructor(val apiRepository: ApiRepository) : ViewModel() {

    var responseLiveData = MutableLiveData<Response<Any?>>()

    init {
        getBlockList()
    }

    private fun getBlockList() {
        viewModelScope.launch {
            apiRepository.getBlockList().let {
                when (it.isRequestSuccess(it.body()?.code)) {
                    AUT -> responseLiveData.postValue(Response(reLogin = true))
                    ERROR -> responseLiveData.postValue(Response(error = "$it"))
                    SUCCESS -> responseLiveData.postValue(Response(data = it.body() , flag = 0))
                }
            }
        }
    }

    fun unBlockUser(user : BlockData){
        viewModelScope.launch {
            apiRepository.unBlockUser("${user.id}")
                .let {
                    when (it.isRequestSuccess(it.body()?.code)) {
                        AUT -> responseLiveData.postValue(Response(reLogin = true))
                        ERROR -> responseLiveData.postValue(Response(error = "$it"))
                        SUCCESS -> responseLiveData.postValue(Response(data = user, flag = 1))
                    }
                }
        }
    }
}