package com.mas.cryptomasters

import androidx.lifecycle.ViewModel
import com.mas.cryptomasters.core.api.ApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BaseViewModel @Inject constructor(apiRepository: ApiRepository) : ViewModel() {


}