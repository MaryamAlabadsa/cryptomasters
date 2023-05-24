package com.mas.cryptomasters

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.mas.BaseActivity
import com.mas.cryptomasters.adapters.ChatAdapter
import com.mas.cryptomasters.core.pref.PreferenceHelper
import com.mas.cryptomasters.data.models.ChatModels
import com.mas.cryptomasters.databinding.ActivityChatBinding
import com.mas.cryptomasters.databinding.ActivityMainBinding
import com.mas.cryptomasters.databinding.LoadingLayoutBinding
import com.mas.cryptomasters.ui.othersActivity.NavigationActivity
import com.mas.cryptomasters.utils.Constants
import com.mas.cryptomasters.utils.Extensions.askLogin
import com.mas.cryptomasters.utils.Extensions.crToast
import com.mas.cryptomasters.utils.Extensions.navigateToActivity
import com.mas.cryptomasters.utils.Extensions.reLogin
import com.mas.cryptomasters.utils.Navigate
import com.mas.cryptomasters.utils.ToastType
import es.dmoral.toasty.Toasty
import java.lang.NullPointerException
import java.util.logging.Logger


class ChatActivity : BaseActivity<ActivityChatBinding>(ActivityChatBinding::inflate) {

    private lateinit var chatAdapter: ChatAdapter
    private var mInterstitialAd: InterstitialAd? = null
    private val adRequest: AdRequest = AdRequest.Builder().build()
    val db = FirebaseFirestore.getInstance()
    lateinit var preferences: PreferenceHelper
    val chatList = ArrayList<ChatModels>()


    override fun setView(phoneIsConnected: Boolean) {
        preferences = PreferenceHelper(this)
        setInternalAds()
        init()


    }

    private fun init() {
        chatAdapter = ChatAdapter(preferences)
        binding.rvList.layoutManager = LinearLayoutManager(this)
        binding.rvList.adapter = chatAdapter

        if (preferences.isGustUser()) {
            binding.linerMessage.visibility = View.GONE
            binding.loading.clLoading.visibility = View.GONE
            binding.rvList.visibility = View.GONE
            binding.clNotUser.visibility = View.VISIBLE
        } else {
            // load interstitial ad first
            InterstitialAd.load(this, Constants.INTERNAL_ADS, adRequest,
                object : InterstitialAdLoadCallback() {
                    override fun onAdFailedToLoad(adError: LoadAdError) {
                        // if ad failed to load, load data and show it immediately
                        loadChatData()
                    }

                    override fun onAdLoaded(interstitialAd: InterstitialAd) {
                        // if ad loaded successfully, show it before loading data
                        mInterstitialAd = interstitialAd
                        mInterstitialAd?.fullScreenContentCallback =
                            object : FullScreenContentCallback() {
                                override fun onAdDismissedFullScreenContent() {
                                    loadChatData()
                                }
                            }
                        mInterstitialAd?.show(this@ChatActivity)
                    }
                }
            )
        }

        binding.ivNotifications.setOnClickListener {
            navigateToNotifications(this)
        }

        binding.tvLogin.setOnClickListener {
            this.reLogin(preferences)
        }
    }

    private fun loadChatData() {
        db.collection("chat")
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    handleLoading(binding.loading, true)
                    return@addSnapshotListener
                }
                chatList.clear()
                for (doc in value!!) {
                    val chat = doc.toObject(ChatModels::class.java)
                    chatList.add(chat)
                }
                try {
                    if (chatList.size > 0) {
                        chatAdapter.updateChats(chatList).apply {
                            binding.rvList.scrollToPosition(chatList.size - 1)
                        }
                    }
                    handleLoading(binding.loading, false)
                } catch (e: NullPointerException) {
                }
            }

        binding.ivSend.setOnClickListener {
            val message = binding.txtMessage.text.toString()
            if (message.isNotEmpty()) {
                if (preferences.getUserProfile() .isPaid != "1") {
                    if (preferences.getAdsCount() >= Constants.ADS_COUNT) {
                        if (mInterstitialAd != null) {
                            mInterstitialAd?.show(this)
                            mInterstitialAd?.fullScreenContentCallback =
                                object : FullScreenContentCallback() {
                                    override fun onAdDismissedFullScreenContent() {
                                        preferences.incrementAdsCount(true)
                                        setInternalAds()
                                        sendMessage(message)
                                    }

                                    override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                                        sendMessage(message)
                                    }
                                }
                        } else {
                            sendMessage(message)
                        }
                    } else {
                        preferences.incrementAdsCount(false)
                        sendMessage(message)
                    }
                } else {
                    sendMessage(message)
                }
            }
        }
    }

    private fun sendMessage(message: String) {
        val chatModels = ChatModels(
            avatar = "${preferences.getUserProfile() .avatar}",
            idUser = preferences.getUserProfile() .id!!,
            message = message,
            name = preferences.getUserProfile() .name.toString(),
        )
        binding.txtMessage.setText("")
        db.collection("chat")
            .add(chatModels)
    }

    private fun setInternalAds() {
        val Log = Logger.getLogger(ChatActivity::class.java.name)
        Log.warning("Hello World")
        InterstitialAd.load(this, Constants.INTERNAL_ADS, adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    mInterstitialAd = null
                    Log.warning("Hello World1212")

                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    mInterstitialAd = interstitialAd
//                    mInterstitialAd!!.show(this@ChatActivity)
                    Log.warning("Hello World6666666")

                }
            })

    }

    fun navigateToNotifications(activity: Activity) {
        if (preferences.isGustUser()) {
            activity.askLogin()
        } else {
            val bundle = Bundle()
            bundle.putString(Navigate.TARGET, Navigate.NOTIFICATION)
            activity.navigateToActivity(NavigationActivity::class.java, bundle = bundle)
        }
    }

    fun handleLoading(binding: LoadingLayoutBinding, isError: Boolean) {
        if (isError) {
            binding.clOnLoading.visibility = View.GONE
            binding.clLoading.visibility = View.VISIBLE
            binding.clOnError.visibility = View.VISIBLE
        } else {
            binding.clLoading.visibility = View.GONE
        }
    }


}