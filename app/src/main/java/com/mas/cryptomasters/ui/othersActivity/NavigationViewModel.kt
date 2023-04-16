package com.mas.cryptomasters.ui.othersActivity

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mas.cryptomasters.core.api.ApiRepository
import com.mas.cryptomasters.data.Response
import com.mas.cryptomasters.data.request.ChangePasswordRequest
import com.mas.cryptomasters.data.request.ContactRequest
import com.mas.cryptomasters.utils.Extensions.isRequestSuccess
import com.mas.cryptomasters.utils.RESPONSE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NavigationViewModel @Inject constructor(private var apiRepository: ApiRepository) : ViewModel() {
    var contactRequest: ContactRequest? = null
    var changePasswordRequest :ChangePasswordRequest? =null

    val contactResponse : MutableLiveData<Response<Any?>> = MutableLiveData()
    val changePasswordResponse : MutableLiveData<Response<Any?>> = MutableLiveData()



    fun contact() {
        viewModelScope.launch {
            apiRepository.contact(contactRequest!!)
                .let {
                    when (it.isRequestSuccess(it.body()?.code)) {
                        RESPONSE.SUCCESS -> contactResponse.postValue(Response(it.body()!!))
                        RESPONSE.ERROR -> contactResponse.postValue(Response(error = "${it.errorBody()}"))
                        RESPONSE.AUT -> contactResponse.postValue(Response(reLogin = true))
                    }
                }
        }
    }


    fun changePassword(){
        viewModelScope.launch {
            apiRepository.changePassword(changePasswordRequest!!)
                .let {
                    when (it.isRequestSuccess(it.body()?.code)) {
                        RESPONSE.SUCCESS -> changePasswordResponse.postValue(Response(it.body()!!))
                        RESPONSE.ERROR -> changePasswordResponse.postValue(Response(error = "${it.errorBody()}"))
                        RESPONSE.AUT -> changePasswordResponse.postValue(Response(reLogin = true))
                    }
                }
        }

    }}