package com.mas.cryptomasters.ui.chart

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels

import com.mas.BaseActivity
import com.mas.cryptomasters.BaseFragment
import com.mas.cryptomasters.R
import com.mas.cryptomasters.databinding.ActivityChartBinding
import com.mas.cryptomasters.ui.splash.SplashViewModel
import com.mas.cryptomasters.utils.Extensions.crToast
import com.mas.cryptomasters.utils.Extensions.reLogin
import dagger.hilt.android.AndroidEntryPoint
import android.content.Intent
import android.view.View
import com.mas.cryptomasters.databinding.LoadingLayoutBinding


@AndroidEntryPoint
class ChartActivity : BaseActivity<ActivityChartBinding>(ActivityChartBinding::inflate) {

    private val viewModel: ChartViewModel by viewModels()

    override fun setView(phoneIsConnected: Boolean) {
        binding.ivBack.setOnClickListener {
            finish()
        }
        if (!phoneIsConnected) {
            crToast(getString(R.string.no_connection))
            return
        }
//        val marketId = intent.getStringExtra("marketId") ?: "bitcoin"
        val low_24h = preferenceHelper.getlow_24h()
        val high_24h = preferenceHelper.gethigh_24h()
        binding.dayRange.text = high_24h + " / " + low_24h
        val marketId = preferenceHelper.getMarketId()
        viewModel.setId(marketId.lowercase())
        // set up the chart
        val chart = binding.chart
        chart.setNoDataText(getString(R.string.no_data))
        chart.setNoDataTextColor(getColor(R.color.black))

        // set up the buttons
        val buttons = listOf(
            binding.day,
            binding.week,
            binding.month,
            binding.threeMonth,
            binding.year,
            binding.fiveYears
        )
        buttons.forEachIndexed { index, button ->
            button.setOnClickListener {
                viewModel.onButtonClicked(index)
            }
        }

        viewModel._chartData.observe(this) { response ->
            when {
                response.reLogin -> {
                    this.reLogin(preferenceHelper)
                }
                response.error.isNotEmpty() -> {
                    handleLoading(binding.loading, true, "")
                    this.reLogin(preferenceHelper)
                }
                response.data != null && response.flag == 1 -> {
                    // update chart with new data
                    handleLoading(binding.loading, false, "")
                    val data = viewModel.getChartData()
                    chart.data = data
                    chart.invalidate()
                }
            }
        }

        viewModel.selectedButtonIndex.observe(this) { index ->
            // update button appearance based on the selected index
            buttons.forEachIndexed { buttonIndex, button ->
                if (buttonIndex == index) {
                    handleLoading(binding.loading, false, "new")
                    button.setBackgroundResource(R.drawable.selected_button_bg)
                    button.setTextColor(getColor(R.color.white))
                } else {
                    button.setBackgroundResource(R.drawable.unselected_button_bg)
                    button.setTextColor(getColor(R.color.them))
                }
            }
        }
    }

    fun handleLoading(binding: LoadingLayoutBinding, isError: Boolean, x: String) {
        if (isError) {
            binding.clOnLoading.visibility = View.GONE

            binding.clLoading.visibility = View.VISIBLE
            binding.clOnError.visibility = View.VISIBLE
        } else if (x.equals("new"))
            binding.clLoading.visibility = View.VISIBLE
        else {
            binding.clLoading.visibility = View.GONE
        }
    }

}

