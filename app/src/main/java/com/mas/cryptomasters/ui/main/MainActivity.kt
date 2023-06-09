package com.mas.cryptomasters.ui.main

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.mas.BaseActivity
import com.mas.cryptomasters.ChatActivity
import com.mas.cryptomasters.R
import com.mas.cryptomasters.data.models.NotificationsModels
import com.mas.cryptomasters.databinding.ActivityMainBinding
import com.mas.cryptomasters.ui.fragment.coin.CoinFragment
import com.mas.cryptomasters.ui.fragment.home.HomeFragment
import com.mas.cryptomasters.ui.fragment.recommend.RecommendFragment
import com.mas.cryptomasters.ui.fragment.rules.RuleFragment
import com.mas.cryptomasters.ui.fragment.settings.SettingsFragment
import com.mas.cryptomasters.ui.othersActivity.NavigationActivity
import com.mas.cryptomasters.utils.Constants
import com.mas.cryptomasters.utils.Extensions.crToast
import com.mas.cryptomasters.utils.Navigate
import dagger.hilt.android.AndroidEntryPoint
import es.dmoral.toasty.Toasty.warning
import java.util.*
import java.util.logging.Logger


@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {
    private val postActions = listOf("like", "comment")
    private var mInterstitialAd: InterstitialAd? = null
    private val adRequest: AdRequest = AdRequest.Builder().build()
    val Log = Logger.getLogger(ChatActivity::class.java.name)


    override fun setView(phoneIsConnected: Boolean) {
        if (!phoneIsConnected) {
            crToast(getString(R.string.no_connection))
            return
        }


        //select home by default
        binding.bottomMenu.setItemSelected(R.id.navigation_home, true)
        binding.bottomMenu.setOnItemSelectedListener {
            when (it) {
                R.id.navigation_home -> setHomeFragment(HomeFragment())
                R.id.navigation_reco -> setHomeFragment(RecommendFragment())
                R.id.navigation_rules -> setHomeFragment(RuleFragment())
                R.id.navigation_setting -> setHomeFragment(SettingsFragment())
                R.id.navigation_coin -> setHomeFragment(CoinFragment())
            }
        }

        handleIfHaveNotifications()

        if (preferenceHelper.getUserProfile() .isPaid != "1") {
//            setAds(binding.viewForAds)
        }
    }


    private fun handleIfHaveNotifications() {
        if (intent.hasExtra(Navigate.HAS_NOTIFICATION) && intent.getParcelableExtra<NotificationsModels>(
                Navigate.HAS_NOTIFICATION
            ) != null
        ) {
            val notify = intent.getParcelableExtra<NotificationsModels>(Navigate.HAS_NOTIFICATION)
            val bundle = Bundle()
            intent = Intent(applicationContext, NavigationActivity::class.java)
            when {
                postActions.indexOf(notify?.action) != -1 -> {
                    bundle.putString(Navigate.TARGET, Navigate.POST_DETAILS)
                    bundle.putString(Navigate.DATA_ID, notify?.post_id)
                    intent.putExtras(bundle)
                }
                notify?.action == "recommend" -> {
                    bundle.putString(Navigate.TARGET, Navigate.RECOMMEND_FRAGMENT)
                    intent.putExtras(bundle)
                }
                else -> {
                    bundle.putString(Navigate.TARGET, Navigate.NOTIFICATION)
                }
            }
            intent.putExtras(bundle)
            startActivity(intent)

        }
    }

    private fun setHomeFragment(target: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.hostContainer, target)
            .commit()
    }

    override fun onBackPressed() {
        val homeFragment = HomeFragment()

        // check if the current fragment is not the home fragment
        if (supportFragmentManager.findFragmentById(R.id.hostContainer) !is HomeFragment) {
            binding.bottomMenu.setItemSelected(R.id.navigation_home, true)
            setHomeFragment(homeFragment) // navigate to the home fragment
        } else {
            super.onBackPressed() // exit the app or finish the activity
        }
    }
}