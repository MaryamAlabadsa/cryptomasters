package com.mas.cryptomasters.ui.fragment.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mas.cryptomasters.core.api.ApiRepository
import com.mas.cryptomasters.data.Response
import com.mas.cryptomasters.data.request.EditProfileRequest
import com.mas.cryptomasters.utils.Extensions.isRequestSuccess
import com.mas.cryptomasters.utils.RESPONSE.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(val apiRepository: ApiRepository) : ViewModel() {
    lateinit var editProfileRequest: EditProfileRequest
    val mutableProfileEdit = MutableLiveData<Response<Any?>>()

    fun updateProfile() {
        viewModelScope.launch {
            apiRepository.updateProfile(editProfileRequest)
                .let {
                    when (it.isRequestSuccess(it.code())) {
                        AUT -> mutableProfileEdit.postValue(Response(error = it.message() + ""))
                        ERROR -> mutableProfileEdit.postValue(Response(reLogin = true))
                        SUCCESS -> mutableProfileEdit.postValue(Response(data = it.body() , flag = 1))
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
                    when (it.isRequestSuccess(it.body()!!.code)) {
                        AUT -> mutableProfileEdit.postValue(Response(error = it.message() + ""))
                        ERROR -> mutableProfileEdit.postValue(Response(reLogin = true))
                        SUCCESS -> mutableProfileEdit.postValue(Response(data = it.body()!!.data , flag = 0))
                    }
                }
        }
    }
}