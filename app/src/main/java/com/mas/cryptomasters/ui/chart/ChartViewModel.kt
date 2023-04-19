package com.mas.cryptomasters.ui.chart

import android.graphics.Color
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.google.gson.Gson
import com.mas.cryptomasters.core.api.api2.ApiRepository2
import com.mas.cryptomasters.data.Response
import com.mas.cryptomasters.data.models.ChartResponse
import com.mas.cryptomasters.data.models.Price
import com.mas.cryptomasters.utils.Extensions.isRequestSuccess
import com.mas.cryptomasters.utils.RESPONSE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.logging.Logger
import javax.inject.Inject

@HiltViewModel
class ChartViewModel @Inject constructor(private val apiRepository: ApiRepository2) : ViewModel() {

    private var marketId: String? = null

    val _chartData: MutableLiveData<Response<Any>> = MutableLiveData()
    private val _prices = MutableLiveData<List<Price>>()
    val prices: LiveData<List<Price>> = _prices

    private val _selectedButtonIndex = MutableLiveData<Int>()
    val selectedButtonIndex: LiveData<Int> = _selectedButtonIndex

    init {
//        Toast.makeText(contect, "kkkkkkkkk", Toast.LENGTH_LONG).show()
        val Log = Logger.getLogger(ChartResponse::class.java.name)
        Log.warning(marketId+"kkkkkkkkkkkkkkkkkk")
        // initialize the chart data and the selected button index
        marketId?.let { loadChartData(it, 1) }
        _selectedButtonIndex.value = 0
    }

    fun setId(id: String) {
        marketId = id
        loadChartData(id, 1)
    }

    fun onButtonClicked(index: Int) {
        _selectedButtonIndex.value = index
        val num = when (index) {
            1 -> 7
            2 -> 30
            3 -> 90
            4 -> 356
            5 -> 5 * 356
            else -> 1
        }
        // load new chart data based on the selected button index
        marketId?.let { loadChartData(it, num) }
    }

    private fun loadChartData(id: String, days: Int) {
        viewModelScope.launch {
            // fetch chart data from API based on the selected button index
            // and update _chartData with the new data
            val response = apiRepository.getMarketChart(id, "usd", days)
            when (response.isRequestSuccess(response.code())) {
                RESPONSE.AUT -> _chartData.postValue(Response(reLogin = true))
                RESPONSE.ERROR -> _chartData.postValue(Response(error = response.toString()))
                RESPONSE.SUCCESS -> _chartData.postValue(
                    Response(
                        data = response.body(),
                        flag = 1
                    )
                )
            }
        }
    }

    fun getChartData(): LineData {
        val chartResponse = _chartData.value?.data as? ChartResponse
        val prices = chartResponse?.prices?.map { Price(it[0] as Double, it[1] as Double) } ?: emptyList()
        _prices.value = prices

        val entries = mutableListOf<Entry>()
        prices.forEach {
            entries.add(Entry(it.timestamp.toFloat(), it.value.toFloat()))
        }

        val lineDataSet = LineDataSet(entries, "Label")
        lineDataSet.color = Color.BLUE

        val dataSets = mutableListOf<ILineDataSet>()
        dataSets.add(lineDataSet)

        return LineData(dataSets)
    }
}

