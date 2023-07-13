package com.mas.cryptomasters.ui.analytic

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mas.BaseActivity
import com.mas.cryptomasters.AnalyticDataActivity
import com.mas.cryptomasters.R
import com.mas.cryptomasters.adapters.AnalyticAdapter
import com.mas.cryptomasters.adapters.RecommendAdapter
import com.mas.cryptomasters.data.response.Analytic
import com.mas.cryptomasters.data.response.recommendations.Recommendations
import com.mas.cryptomasters.databinding.ActivityAnalyticBinding
import com.mas.cryptomasters.databinding.ActivityLoginBinding
import com.mas.cryptomasters.ui.login.LoginViewModel
import com.mas.cryptomasters.ui.othersActivity.NavigationActivity
import com.mas.cryptomasters.utils.Extensions
import com.mas.cryptomasters.utils.Extensions.crToast
import com.mas.cryptomasters.utils.Navigate
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AnalyticActivity : BaseActivity<ActivityAnalyticBinding>(ActivityAnalyticBinding::inflate) {
    private val viewModel: AnalyticViewModel by viewModels()
    private lateinit var analyticAdapter: AnalyticAdapter

    override fun setView(phoneIsConnected: Boolean) {
        handleRV()
        binding.ivBack.setOnClickListener(View.OnClickListener { finish() })
        viewModel.init()
        viewModel.mutableRecommend.observe(this) { result ->
            when {
                result.data != null -> {
                    handleLoading(binding.loading, false)
                    handleResponse(result.data as Analytic)
                }
                result.reLogin -> {
                    handleLoading(binding.loading, true)
                    Extensions.hideProgress()
                }
                result.error.isNotEmpty() -> {
                    handleLoading(binding.loading, true)
                    Extensions.hideProgress()
                }
            }

        }
    }

    private fun handleRV() {
        analyticAdapter = AnalyticAdapter()
        binding.rvAnalytics.layoutManager = LinearLayoutManager(this)
        binding.rvAnalytics.adapter = analyticAdapter
        analyticAdapter.currentData.observe(this) { data ->
            intentToAnalyticData()
        }


    }

    private fun handleResponse(analytic: Analytic) {
//        preferences.setRecommend(recommendations.recommendData)
        analytic.analyticData.let {
            analyticAdapter.setList(it)
        }
    }

    private fun intentToAnalyticData() {
        val intent = Intent(this, AnalyticDataActivity::class.java)
        intent.putExtra(Navigate.DATA_POST, analyticAdapter.currentData.value)
        startActivity(intent)
    }


}
