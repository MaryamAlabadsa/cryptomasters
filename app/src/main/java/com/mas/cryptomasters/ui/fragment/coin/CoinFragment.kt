package com.mas.cryptomasters.ui.fragment.coin

import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.mas.cryptomasters.BaseFragment
import com.mas.cryptomasters.adapters.CoinAdapter
import com.mas.cryptomasters.data.models.coin.Coins
import com.mas.cryptomasters.data.response.CoinsResponse
import com.mas.cryptomasters.databinding.FragmentCoinsBinding
import com.mas.cryptomasters.utils.Extensions.crToast
import com.mas.cryptomasters.utils.Extensions.reLogin
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CoinFragment : BaseFragment<FragmentCoinsBinding>() {
    private val viewModel: CoinViewModel by viewModels()

    //method inflates the FragmentNotificationsBinding layout, which contains a RecyclerView for displaying the list of coins.
    override fun getViewBinding() = FragmentCoinsBinding.inflate(layoutInflater)

    private lateinit var coinsAdapter: CoinAdapter


    override fun init() {
        coinsAdapter = CoinAdapter(preferences, requireActivity())
        binding.rvList.layoutManager = LinearLayoutManager(requireActivity())
        binding.rvList.adapter = coinsAdapter

        viewModel.coinMutable.observe(this) {
            when {
                it.reLogin -> requireActivity().reLogin(preferences)
                it.error.isNotEmpty() -> {
                    handleLoading(binding.loading, true)
                    requireActivity().crToast()
                }
                it.data != null && it.flag == 1 -> {
                    coinsAdapter.updateAdapter((it.data as List<Coins>))
                    handleLoading(binding.loading, false)
                }
            }
        }
    }

}