package com.mas.cryptomasters.ui.fragment.rules

import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.Visibility
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.material.tabs.TabLayout
import com.mas.cryptomasters.BaseFragment
import com.mas.cryptomasters.adapters.CoinsAdapter
import com.mas.cryptomasters.adapters.NotificationsAdapter
import com.mas.cryptomasters.data.response.CoinsResponse
import com.mas.cryptomasters.data.response.SearchResponse
import com.mas.cryptomasters.data.response.home.Coin
import com.mas.cryptomasters.databinding.FragmentRecommendBinding
import com.mas.cryptomasters.databinding.FragmentRuleBinding
import com.mas.cryptomasters.databinding.LoadingLayoutBinding
import com.mas.cryptomasters.ui.othersActivity.NavigationActivity
import com.mas.cryptomasters.utils.Constants
import com.mas.cryptomasters.utils.Extensions.crToast
import com.mas.cryptomasters.utils.Extensions.reLogin
import com.mas.cryptomasters.utils.Navigate
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RuleFragment : BaseFragment<FragmentRuleBinding>() {

    override fun getViewBinding() = FragmentRuleBinding.inflate(layoutInflater)

    private lateinit var coinsAdapter: CoinsAdapter
    private var coinList: ArrayList<Coin> = ArrayList()
    private var listedCoinList: ArrayList<Coin> = ArrayList()
    private var searchCoinList: ArrayList<Coin> = ArrayList()
    private var position = 0

    val viewModel: RuleViewModel by viewModels()


    private var mInterstitialAd: InterstitialAd? = null
    private val adRequest: AdRequest = AdRequest.Builder().build()

    override fun init() {

        coinsAdapter = CoinsAdapter()
        binding.rvList.layoutManager = LinearLayoutManager(requireContext())
        binding.rvList.adapter = coinsAdapter

        binding.svSearch.setOnClickListener {
            binding.svSearch.isIconified = false;
        }
        val coinsData = preferences.getRuleContent()
        if (coinsData.isEmpty()) {
            viewModel.mutableRules.observe(this) { rules ->
                when {
                    rules.reLogin -> requireActivity().reLogin(preferences)
                    rules.error.isNotEmpty() -> {
                        handleLoading(binding.loading, true)
                        requireContext().crToast()
                    }
                    else -> {
                        handleLoading(binding.loading, false)
                        val coins = rules.data?.let { data ->
                            when (data) {
                                is CoinsResponse -> data.coins
                                is SearchResponse -> data.coins
                                else -> emptyList()
                            }
                        } ?: emptyList()
                        preferences.setRuleContent(coins)
                        updateCoinsAdapter(coins)
                    }
                }
            }
        } else {
//            Extensions.hideProgress()
            handleLoading(binding.loading, false)
            updateCoinsAdapter(coinsData)

//            coinsAdapter.updateAdapter(homePosts) // Update the adapter with the stored data
        }
        //when get result
        coinsAdapter.updateAdapter(coinList, binding.svSearch.query.toString())

        binding.svSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                coinsAdapter.clearAdapter()
                handleLoading(binding.loading, false, "new")
                if (query != null) {
                    viewModel.searchCoins(query)
                } else {
                    viewModel.getAllCoins()
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
        binding.svSearch.setOnCloseListener {
            coinsAdapter.clearAdapter()
            handleLoading(binding.loading, false, "new")
            viewModel.getAllCoins()
            true
        }

        // check the tap
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {
                        position = 0
                        coinsAdapter.updateAdapter(coinList)
                    }
                    1 -> {
                        position = 1
                        sortList("0")
                    }
                    2 -> {
                        position = 2
                        sortList("1")
                    }
                }
            }

            //
            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })

        binding.top.ivNotifications.setOnClickListener {
            navigateToNotifications(requireActivity())
        }


        coinsAdapter.mutableSelectedCoin.observe(this) {
            if (it != null) {
                if (preferences.getUserProfile().isPaid != "1") {
                    if (mInterstitialAd != null) {
                        mInterstitialAd?.show(requireActivity())
                        mInterstitialAd?.fullScreenContentCallback =
                            object : FullScreenContentCallback() {
                                override fun onAdDismissedFullScreenContent() {
                                    intentToRule()
                                }

                                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                                    intentToRule()
                                }
                            }
                    } else {
                        intentToRule()
                    }

                } else {
                    intentToRule()
                }
            }
        }
    }

    private fun sortList(status: String) {
        listedCoinList.clear()

        for (item in coinList) {
            if (item.isHalal == status) {
                listedCoinList.add(item)
            }
        }
        coinsAdapter.updateAdapter(listedCoinList)
    }

    private fun setInternalAds() {
        InterstitialAd.load(requireContext(), Constants.INTERNAL_ADS, adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    mInterstitialAd = null
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    mInterstitialAd = interstitialAd
                }
            })

    }

    private fun intentToRule() {
        requireActivity().startActivity(
            Intent(context, NavigationActivity::class.java)
                .putExtra(Navigate.TARGET, Navigate.COIN_RULE)
                .putExtra(Navigate.COIN_DATA, coinsAdapter.currentCoin!!)
        )

    }

    override fun onResume() {
        super.onResume()
        setInternalAds()
    }

    private fun updateCoinsAdapter(coins: List<Coin>) {
        coinList.clear()
        coinList.addAll(coins)
        coinsAdapter.updateAdapter(coinList)
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