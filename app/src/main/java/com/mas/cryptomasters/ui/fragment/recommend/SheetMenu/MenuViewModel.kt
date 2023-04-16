package com.mas.cryptomasters.ui.fragment.recommend.SheetMenu

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mas.cryptomasters.core.api.ApiRepository
import com.mas.cryptomasters.data.Response
import com.mas.cryptomasters.data.models.PlanResponse
import com.mas.cryptomasters.utils.Extensions.isRequestSuccess
import com.mas.cryptomasters.utils.RESPONSE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(val apiRepository: ApiRepository) : ViewModel() {
    val menuMutable: MutableLiveData<Response<PlanResponse>> = MutableLiveData()

    init {
        getMenuContent()
    }

    private fun getMenuContent() {
        viewModelScope.launch {
            apiRepository.getMenuItems().let {
                when (it.isRequestSuccess(it.code())) {
                    RESPONSE.AUT -> menuMutable.postValue(Response(reLogin = true))
                    RESPONSE.ERROR -> menuMutable.postValue(Response(error = it.toString()))
                    RESPONSE.SUCCESS -> menuMutable.postValue(
                        Response(
                            data = it.body(),
                            flag = 1
                        )
                    )
                }
            }
        }
    }
}
