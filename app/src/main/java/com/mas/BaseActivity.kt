package com.mas

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewbinding.ViewBinding
import com.google.android.gms.ads.*
import com.google.gson.Gson
import com.mas.cryptomasters.core.pref.PreferenceHelper
import com.mas.cryptomasters.data.models.NotificationsModels
import com.mas.cryptomasters.utils.Constants
import com.mas.cryptomasters.utils.Extensions.hideProgress
import com.mas.cryptomasters.utils.Extensions.isPhoneIsConnected
import com.mas.cryptomasters.utils.LocaleHelper
import com.mas.cryptomasters.utils.Navigate
import javax.inject.Inject

abstract class BaseActivity<B : ViewBinding>(val bindingFactory: (LayoutInflater) -> B) : AppCompatActivity() {

    lateinit var binding: B
    val gson = Gson()

    @Inject
    lateinit var preferenceHelper: PreferenceHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = bindingFactory(layoutInflater)
        setContentView(binding.root)
        setView(isPhoneIsConnected())
    }

    abstract fun setView(phoneIsConnected: Boolean)

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(LocaleHelper.onAttach(base))
    }




//    fun setAds(view: ConstraintLayout) {
//        val mAdView = AdView(applicationContext)
//        mAdView.adSize = AdSize.BANNER
//        mAdView.adUnitId = Constants.BANNER_ADS
//        val adRequest: AdRequest = AdRequest.Builder()
//            .build()
//        mAdView.loadAd(adRequest)
//        mAdView.adListener = object : AdListener() {
//            override fun onAdLoaded() {
//                view.addView(mAdView)
//                view.visibility = View.VISIBLE
//            }
//
//            override fun onAdFailedToLoad(p0: LoadAdError) {
//                super.onAdFailedToLoad(p0)
//                view.visibility = View.GONE
//            }
//        }
//
//    }


    fun navigateToActivity(
        javaClass: Class<*>,
        finish: Boolean = false,
        data: NotificationsModels? = null
    ) {
        val intent = Intent(applicationContext, javaClass)
        if (finish) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            finish()
        }
        if (data != null) {
            intent.putExtra(Navigate.HAS_NOTIFICATION, data)
        }

        startActivity(intent)
    }

    override fun onDestroy() {
        hideProgress()
        super.onDestroy()

    }


}