package com.mas.cryptomasters.ui.fragment.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.mas.cryptomasters.BaseFragment
import com.mas.cryptomasters.ChatActivity
import com.mas.cryptomasters.R
import com.mas.cryptomasters.adapters.PostsAdapter
import com.mas.cryptomasters.data.Response
import com.mas.cryptomasters.data.response.home.HomeResponse
import com.mas.cryptomasters.databinding.FragmentHomeBinding
import com.mas.cryptomasters.ui.fragment.posts.PostsViewModel
import com.mas.cryptomasters.ui.othersActivity.NavigationActivity
import com.mas.cryptomasters.utils.*
import com.mas.cryptomasters.utils.Extensions.crToast
import com.mas.cryptomasters.utils.Extensions.hideProgress
import com.mas.cryptomasters.utils.Extensions.loadWebImage
import com.mas.cryptomasters.utils.Extensions.navigateToActivity
import com.mas.cryptomasters.utils.Extensions.reLogin
import com.mas.cryptomasters.utils.Extensions.setTextWithTowColor
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    private val viewModel: HomeFragmentViewModel by viewModels()
    private val postViewModel: PostsViewModel by viewModels()
    override fun getViewBinding() = FragmentHomeBinding.inflate(layoutInflater)
    private var mInterstitialAd: InterstitialAd? = null
    private val adRequest: AdRequest = AdRequest.Builder().build()

    private lateinit var postsAdapter: PostsAdapter
    override fun init() {
//        Toast.makeText(context, "kkkk", Toast.LENGTH_SHORT).show()


        requireContext().setTextWithTowColor(
            binding.tvCreatePost,
            string1 = getString(R.string.what_think1),
            string2 = getString(R.string.what_think2)
        )

        postsAdapter = PostsAdapter(requireActivity(), postViewModel, this, parentFragmentManager)
        binding.rvPost.layoutManager = LinearLayoutManager(requireActivity())
        binding.rvPost.adapter = postsAdapter


        // go to post activity
        binding.tvCreatePost.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(Navigate.TARGET, Navigate.CREATE_POST)
            requireActivity().navigateToActivity(NavigationActivity::class.java, bundle = bundle)
        }



        setUserData()
        // handle all activity response
        viewModel.homeMutable.observe(this) { homeResponse ->
            if (mInterstitialAd == null) {
                InterstitialAd.load(requireContext(), Constants.INTERNAL_ADS, adRequest,
                    object : InterstitialAdLoadCallback() {
                        override fun onAdFailedToLoad(adError: LoadAdError) {
                            mInterstitialAd = null
                            handleHomeResponse(homeResponse)
                        }

                        override fun onAdLoaded(interstitialAd: InterstitialAd) {
                            mInterstitialAd = interstitialAd
                            mInterstitialAd!!.show(requireActivity())
                            mInterstitialAd?.fullScreenContentCallback =
                                object : FullScreenContentCallback() {
                                    override fun onAdDismissedFullScreenContent() {
                                        handleHomeResponse(homeResponse)
                                    }

                                    override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                                        handleHomeResponse(homeResponse)
                                    }
                                }
                        }
                    })
            } else {
                handleHomeResponse(homeResponse)
            }

        }


        binding.ivNotifications.setOnClickListener {
            navigateToNotifications(requireActivity())
        }
        binding.ivChat.setOnClickListener {
            val intent = Intent(requireActivity(), ChatActivity::class.java)
            startActivity(intent)
        }
        binding.clComment.setOnRefreshListener {
            binding.clComment.isRefreshing = true
            viewModel.getHomeContent()
        }
    }

    private fun handleHomeResponse(homeResponse: Response<Any?>) {
        hideProgress()
        if (binding.clComment.isRefreshing) {
            binding.clComment.isRefreshing = false
            requireActivity().crToast(
                getString(R.string.home_page_update),
                ToastType.SUCCESS
            )
        }
        when {
            homeResponse.reLogin -> {
                requireActivity().reLogin(preferences)
            }
            homeResponse.error.isNotEmpty() -> if (homeResponse.flag != 2) {
                requireContext().crToast()
                handleLoading(binding.loading, true)
            }
            // get home
            homeResponse.flag == 0 -> {
                handleLoading(binding.loading, false)
                postsAdapter.updateAdapter((homeResponse.data as HomeResponse).data.posts)
//                postsAdapter.updateAdapter(homeResponse.data.posts)
            }
        }
    }


    private fun setUserData() {
        if (preferences.isGustUser()) {
            binding.tvName.text = getString(R.string.gust_user)
            binding.tvMobile.text = "---"
            binding.tvCreatePost.visibility = View.GONE
        } else {
            preferences.getUserProfile().let {
                binding.ivProfile.loadWebImage(it.avatar + "")
                binding.tvName.text = it.name
                binding.tvMobile.text = it.phone
            }
        }
    }

    override fun onResume() {
        super.onResume()
//        setInternalAds()
        // to check if there any updates
        preferences.getSomeUpdate().let {
            if (it.whereData != null) {
                postsAdapter.updateSinglePost(it.whereData)
            }
            preferences.clearUpdate()
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
                    mInterstitialAd!!.show(requireActivity())
                }
            })
    }

    override fun onStart() {
        super.onStart()
        setInternalAds()
    }

}