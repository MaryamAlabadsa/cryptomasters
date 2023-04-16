package com.mas.cryptomasters.ui.splash

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mas.cryptomasters.core.api.ApiRepository
import com.mas.cryptomasters.data.Response
import com.mas.cryptomasters.data.request.VisitorRequest
import com.mas.cryptomasters.utils.Extensions.isRequestSuccess
import com.mas.cryptomasters.utils.RESPONSE.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(private val apiRepository: ApiRepository) : ViewModel() {
    val splashResponse = MutableLiveData<Response<Any?>>()
    var visitorRequest: VisitorRequest? = null

    fun getAppSettings() {
        try {
            viewModelScope.launch {
                apiRepository.getSettings()
                    .let {
                        when (it.isRequestSuccess(it.body()?.code)) {
                            AUT -> splashResponse.postValue(Response(reLogin = true))
                            ERROR -> splashResponse.postValue(Response(error = it.toString()))
                            SUCCESS -> splashResponse.postValue(
                                Response(
                                    data = it.body(),
                                    flag = 0
                                )
                            )
                        }
                    }
            }
        } catch (e: NullPointerException) {
            splashResponse.postValue(Response(error = "$e"))
        }
    }

    fun loginAsVisitor() {
        try {
            viewModelScope.launch {
                apiRepository.loginAsVisitor(visitorRequest!!)
                    .let {
                        when (it.isRequestSuccess(it.body()?.code)) {
                            AUT -> splashResponse.postValue(Response(reLogin = true))
                            ERROR -> splashResponse.postValue(Response(error = it.toString()))
                            SUCCESS -> splashResponse.postValue(
                                Response(
                                    data = it.body(),
                                    flag = 2
                                )
                            )
                        }
                    }
            }
        } catch (e: NullPointerException) {
            splashResponse.postValue(Response(error = "$e"))
        }

    }

    fun getUserProfile() {
        try {
            viewModelScope.launch {
                apiRepository.getProfile()
                    .let {
                        when (it.isRequestSuccess(it.body()?.code)) {
                            AUT -> splashResponse.postValue(Response(reLogin = true))
                            ERROR -> splashResponse.postValue(Response(error = it.toString()))
                            SUCCESS -> splashResponse.postValue(
                                Response(
                                    data = it.body()?.profileObject!!,
                                    flag = 1
                                )
                            )
                        }
                    }
            }
        } catch (e: NullPointerException) {
            splashResponse.postValue(Response(error = "$e"))
        }

    }


}