package com.mas.cryptomasters.ui.forgetPassword

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mas.cryptomasters.core.api.ApiRepository
import com.mas.cryptomasters.data.request.ResetPasswordRequest
import com.mas.cryptomasters.utils.Extensions.isRequestSuccess
import com.mas.cryptomasters.utils.RESPONSE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewPasswordViewModel @Inject constructor(val apiRepository: ApiRepository) : ViewModel() {
    lateinit var resetPasswordRequest: ResetPasswordRequest
     var passwordRestMutable = MutableLiveData<RESPONSE>()

    fun reset() {
        viewModelScope.launch {
            apiRepository.resetPassword(resetPasswordRequest)
                .let {
                    passwordRestMutable.postValue(it.isRequestSuccess(it.code()))
                }
        }
    }
}