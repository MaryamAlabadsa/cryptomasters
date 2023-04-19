package com.mas.cryptomasters.ui.fragment.recommend

import android.content.Intent
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.mas.cryptomasters.BaseFragment
import com.mas.cryptomasters.R
import com.mas.cryptomasters.adapters.RecommendAdapter
import com.mas.cryptomasters.data.response.recommendations.Recommendations
import com.mas.cryptomasters.databinding.FragmentRecommendBinding
import com.mas.cryptomasters.databinding.FragmentRecommendDetailsBinding
import com.mas.cryptomasters.ui.fragment.recommend.SheetMenu.MenuViewModel
import com.mas.cryptomasters.ui.fragment.recommend.SheetMenu.SheetMenuDialog
import com.mas.cryptomasters.ui.othersActivity.NavigationActivity
import com.mas.cryptomasters.utils.Constants
import com.mas.cryptomasters.utils.Extensions.crToast
import com.mas.cryptomasters.utils.Extensions.reLogin
import com.mas.cryptomasters.utils.Navigate
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class RecommendFragment : BaseFragment<FragmentRecommendBinding>() {

    private val viewModel: RecommendViewModel by viewModels()
    private lateinit var recommendAdapter: RecommendAdapter

    private var mInterstitialAd: InterstitialAd? = null
    private val adRequest: AdRequest = AdRequest.Builder().build()

    override fun getViewBinding() = FragmentRecommendBinding.inflate(layoutInflater)
    private val menuViewModel: MenuViewModel by viewModels()
    private lateinit var sheetMenuDialog: SheetMenuDialog

    override fun init() {
        if (preferences.isGustUser()) {
            handleLoading(binding.loading, false)

            binding.rvList.visibility = View.GONE
            binding.linear.visibility = View.VISIBLE

            sheetMenuDialog = SheetMenuDialog(menuViewModel)
            binding.linear.setOnClickListener {
                sheetMenuDialog.show(parentFragmentManager, "SheetMenuDialog")
            }


        } else {
            if (arguments?.containsKey(Navigate.TARGET) == true) {
                if (requireArguments().getString(Navigate.TARGET) == Navigate.RECOMMEND_FRAGMENT) {
                    binding.clTop.visibility = View.GONE
                }
            }
            recommendAdapter = RecommendAdapter()
            binding.rvList.layoutManager = LinearLayoutManager(requireContext())
            binding.rvList.adapter = recommendAdapter

            viewModel.mutableRecommend.observe(this) {
                if (this.isVisible) {
                    when {
                        it.reLogin -> requireActivity().reLogin(preferences)
                        it.error.isNotEmpty() -> {
                            handleLoading(binding.loading, true)
                            requireContext().crToast()
                        }
                        it.data != null -> {
                            handleLoading(binding.loading, false)
                            handleResponse(it.data as Recommendations)
                        }
                    }
                }

            }


            recommendAdapter.showAdsObservable.observe(this) {
                if (preferences.getUserProfile().isPaid != "1") {
                    if (mInterstitialAd != null) {
                        mInterstitialAd?.show(requireActivity())
                        mInterstitialAd?.fullScreenContentCallback =
                            object : FullScreenContentCallback() {
                                override fun onAdDismissedFullScreenContent() {
                                    intentToRecommendData()
                                }

                                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                                    intentToRecommendData()
                                }
                            }
                    } else {
                        intentToRecommendData()
                    }

                } else {
                    intentToRecommendData()
                }
            }

            binding.top.ivNotifications.setOnClickListener {
                navigateToNotifications(requireActivity())
            }
        }

    }

    private fun intentToRecommendData() {
        requireActivity().startActivity(
            Intent(context, NavigationActivity::class.java)
                .putExtra(Navigate.TARGET, Navigate.RECOMMEND_DATA)
                .putExtra(Navigate.DATA_POST, recommendAdapter.currentData!!)
        )

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

    private fun handleResponse(recommendations: Recommendations) {
        recommendAdapter.updateAdapter(recommendations.recommendData)
    }

    override fun onResume() {
        super.onResume()
        setInternalAds()

        if (preferences.getSomeUpdate().isThereRecommendUpdate != null) {
            binding.loading.clLoading.visibility = View.VISIBLE
            viewModel.getRecommendList()
            preferences.clearUpdate()
        }
    }

}