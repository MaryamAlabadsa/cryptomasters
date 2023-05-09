package com.mas.cryptomasters.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mas.cryptomasters.core.api.ApiRepository
import com.mas.cryptomasters.data.Response
import com.mas.cryptomasters.data.request.LoginRequest
import com.mas.cryptomasters.data.response.ProfileResponse
import com.mas.cryptomasters.utils.Extensions.isRequestSuccess
import com.mas.cryptomasters.utils.RESPONSE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private var apiRepository: ApiRepository) : ViewModel() {
    lateinit var loginRequest: LoginRequest
    var profile: MutableLiveData<Response<Any?>> = MutableLiveData()

//    fun startLogin() {
//        viewModelScope.launch {
//            apiRepository.login(loginRequest)
//                .let {
//                    when (it.isRequestSuccess(it.code())) {
//                        RESPONSE.SUCCESS -> profile.postValue(Response(data = it.body()!!))
//                        RESPONSE.ERROR -> profile.postValue(Response(error = "$it"))
//                        RESPONSE.AUT -> profile.postValue(Response(reLogin = true))
//                    }
//                }
//        }
//    }
    fun startLogin() {
        viewModelScope.launch {
            apiRepository.login(loginRequest)
                .let { response ->
                    when {
                        response.isSuccessful && response.body()?.code == 200 -> {
                            profile.postValue(Response(data = response.body()!!))
                        }
                        response.isSuccessful && response.body()?.code != 200 -> {
                            profile.postValue(response.body()?.message?.let { Response(error = it) })
                        }
                        else -> {
                            profile.postValue(Response(reLogin = true))
                        }
                    }
                }
        }
    }

}