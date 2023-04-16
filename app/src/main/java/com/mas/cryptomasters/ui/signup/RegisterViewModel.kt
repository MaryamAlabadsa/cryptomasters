package com.mas.cryptomasters.ui.signup

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mas.cryptomasters.core.api.ApiRepository
import com.mas.cryptomasters.data.Response
import com.mas.cryptomasters.data.request.RegisterRequest
import com.mas.cryptomasters.data.response.ProfileResponse
import com.mas.cryptomasters.utils.Extensions.isRequestSuccess
import com.mas.cryptomasters.utils.RESPONSE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.lang.NullPointerException
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val apiRepository: ApiRepository) :
    ViewModel() {
    lateinit var registerReq: RegisterRequest

    var error: MutableLiveData<String> = MutableLiveData()
    var registeraError: MutableLiveData<String> = MutableLiveData()
    var profile: MutableLiveData<ProfileResponse> = MutableLiveData()


    var registerResponse: MutableLiveData<Response<Any?>> = MutableLiveData()

    fun register() {
        viewModelScope.launch {
            apiRepository.register(registerReq).let {
                try {
                    when (it.isRequestSuccess(it.code())) {
                        RESPONSE.AUT -> registerResponse.postValue(Response(error = it.message() + ""))
                        RESPONSE.ERROR -> registerResponse.postValue(Response(reLogin = true))
                        RESPONSE.SUCCESS -> registerResponse.postValue(Response(data = it.body(), flag = 1))
                    }
                } catch (e: NullPointerException) {
                    registerResponse.postValue(Response(error = "$e"))
                }
            }
        }
    }

    fun uploadImage(
        description: RequestBody,
        file: MultipartBody.Part) {
        viewModelScope.launch {
            apiRepository.uploadImage(description, file)
                .let {
                    when (it.isRequestSuccess(it.code())) {
                        RESPONSE.AUT -> registerResponse.postValue(Response(error = it.message() + ""))
                        RESPONSE.ERROR -> registerResponse.postValue(Response(reLogin = true))
                        RESPONSE.SUCCESS -> registerResponse.postValue(Response(data = it.body()!!.data , flag = 0))
                    }
                }
        }
    }
}