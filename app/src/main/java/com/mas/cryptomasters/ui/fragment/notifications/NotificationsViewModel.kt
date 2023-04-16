package com.mas.cryptomasters.ui.fragment.notifications

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mas.cryptomasters.core.api.ApiRepository
import com.mas.cryptomasters.data.Response
import com.mas.cryptomasters.data.response.notifications.NotificationsData
import com.mas.cryptomasters.utils.Extensions.isRequestSuccess
import com.mas.cryptomasters.utils.RESPONSE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationsViewModel @Inject constructor(val apiRepository: ApiRepository) : ViewModel() {
    val mutableNotifications = MutableLiveData<Response<Any?>>()

    init {
        getNotifications()
    }

    private fun getNotifications() {
        viewModelScope.launch {
            apiRepository.getNotifications().let {
                when (it.isRequestSuccess(it.body()!!.code)) {
                    RESPONSE.AUT -> mutableNotifications.postValue(Response(reLogin = true))
                    RESPONSE.ERROR -> mutableNotifications.postValue(Response(error = it.toString()))
                    RESPONSE.SUCCESS -> mutableNotifications.postValue(
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

    fun deleteNotifications(notification: NotificationsData) {
        viewModelScope.launch {
            apiRepository.deleteNotifications("${notification.id}")
                .let {
                    when (it.isRequestSuccess(it.body()!!.code)) {
                        RESPONSE.AUT -> mutableNotifications.postValue(Response(reLogin = true))
                        RESPONSE.ERROR -> mutableNotifications.postValue(Response(error = it.toString()))
                        RESPONSE.SUCCESS -> mutableNotifications.postValue(
                            Response(
                                data = notification,
                                flag = -1
                            )
                        )
                    }
                }
        }
    }
}