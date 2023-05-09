package com.mas.cryptomasters.ui.fragment.coin

import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.mas.cryptomasters.BaseFragment
import com.mas.cryptomasters.adapters.CoinAdapter
import com.mas.cryptomasters.data.Response
import com.mas.cryptomasters.data.models.coin.Coins
import com.mas.cryptomasters.databinding.FragmentCoinsBinding
import com.mas.cryptomasters.utils.Constants
import com.mas.cryptomasters.utils.Extensions.crToast
import com.mas.cryptomasters.utils.Extensions.reLogin
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CoinFragment : BaseFragment<FragmentCoinsBinding>() {
    private val viewModel: CoinViewModel by viewModels()

    //method inflates the FragmentNotificationsBinding layout, which contains a RecyclerView for displaying the list of coins.
    override fun getViewBinding() = FragmentCoinsBinding.inflate(layoutInflater)

    private lateinit var coinsAdapter: CoinAdapter
    private var mInterstitialAd: InterstitialAd? = null
    private val adRequest: AdRequest = AdRequest.Builder().build()

    override fun init() {
        coinsAdapter = CoinAdapter(preferences, requireActivity())
        binding.rvList.layoutManager = LinearLayoutManager(requireActivity())
        binding.rvList.adapter = coinsAdapter

        viewModel.coins.observe(this) { coins ->
            if (mInterstitialAd == null) {
                InterstitialAd.load(requireContext(), Constants.INTERNAL_ADS, adRequest,
                    object : InterstitialAdLoadCallback() {
                        override fun onAdFailedToLoad(adError: LoadAdError) {
                            mInterstitialAd = null
                            handleCoins(coins)
                        }

                        override fun onAdLoaded(interstitialAd: InterstitialAd) {
                            mInterstitialAd = interstitialAd
                            mInterstitialAd!!.show(requireActivity())
                            mInterstitialAd?.fullScreenContentCallback =
                                object : FullScreenContentCallback() {
                                    override fun onAdDismissedFullScreenContent() {
                                        handleCoins(coins)
                                    }

                                    override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                                        handleCoins(coins)
                                    }
                                }
                        }
                    })
            } else {
                handleCoins(coins)
            }
        }

        // Initialize pagination logic
        viewModel.initPagination()

        // Set up scroll listener to fetch next page of items
//    binding.rvList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//            super.onScrolled(recyclerView, dx, dy)
//            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
//            val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
//            if (lastVisibleItemPosition == coinsAdapter.itemCount - 1) {
//                viewModel.fetchNextPage()
//            }
//        }
//    })
    }

    private fun handleCoins(coins: Response<List<Coins>>) {
        when {
            coins.reLogin -> requireActivity().reLogin(preferences)
            coins.error.isNotEmpty() -> {
                handleLoading(binding.loading, true)
                requireActivity().crToast()
            }
            coins.data != null && coins.flag == 1 -> {
                coinsAdapter.updateAdapter(coins.data as List<Coins>)
                handleLoading(binding.loading, false)
            }
        }
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

    override fun onStart() {
        super.onStart()
        setInternalAds()
    }
}