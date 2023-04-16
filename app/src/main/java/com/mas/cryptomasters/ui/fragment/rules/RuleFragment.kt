package com.mas.cryptomasters.ui.fragment.rules

import android.content.Intent
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
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
import com.mas.cryptomasters.data.response.home.Coin
import com.mas.cryptomasters.databinding.FragmentRecommendBinding
import com.mas.cryptomasters.databinding.FragmentRuleBinding
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
        //when get result
        viewModel.mutableRules.observe(this) {
            when {
                it.reLogin -> requireActivity().reLogin(preferences)
                it.error.isNotEmpty() -> {
                    handleLoading(binding.loading, true)
                    requireContext().crToast()
                }
                it.data != null -> {
                    handleLoading(binding.loading, false)

                    coinList.clear()
                    coinList.addAll((it.data as CoinsResponse).coins)
                    coinsAdapter.updateAdapter(coinList)
                }
            }
        }

        // search
        binding.svSearch.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchCoinList.clear()

                if (newText?.length!! >= 2) {

                    if (position == 1 || position == 2) {
                        for (item in listedCoinList) {
                            if (item.name.contains(newText)) {
                                searchCoinList.add(item)
                            }
                        }
                        coinsAdapter.updateAdapter(searchCoinList)
                    } else {
                        for (item in coinList) {
                            if (item.name.contains(newText)) {
                                searchCoinList.add(item)
                            }
                        }
                        coinsAdapter.updateAdapter(searchCoinList)
                    }
                }
                return false
            }
        })
        binding.svSearch.setOnCloseListener {
            if (position == 1 || position == 2) {
                coinsAdapter.updateAdapter(listedCoinList)
            } else {
                coinsAdapter.updateAdapter(coinList)
            }
            false
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


}