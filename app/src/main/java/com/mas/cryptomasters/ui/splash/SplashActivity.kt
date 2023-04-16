package com.mas.cryptomasters.ui.splash

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.viewModels
import com.mas.BaseActivity
import com.mas.cryptomasters.R
import com.mas.cryptomasters.data.models.NotificationsModels
import com.mas.cryptomasters.data.request.VisitorRequest
import com.mas.cryptomasters.data.response.ProfileObject
import com.mas.cryptomasters.data.response.SettingsResponse
import com.mas.cryptomasters.databinding.ActivitySplashBinding
import com.mas.cryptomasters.extinction.Extinction.Companion.animateView
import com.mas.cryptomasters.ui.login.LoginActivity
import com.mas.cryptomasters.ui.main.MainActivity
import com.mas.cryptomasters.utils.Extensions.crToast
import com.mas.cryptomasters.utils.Extensions.reLogin
import com.mas.cryptomasters.utils.Extensions.registerToken
import com.mas.cryptomasters.utils.Extensions.showProgress
import com.mas.cryptomasters.utils.LocaleHelper
import dagger.hilt.android.AndroidEntryPoint


@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : BaseActivity<ActivitySplashBinding>(ActivitySplashBinding::inflate) {

    private val viewModel: SplashViewModel by viewModels()
    override fun setView(phoneIsConnected: Boolean) {

        if (!phoneIsConnected){
            crToast(getString(R.string.no_connection))
            return
        }

        LocaleHelper.setLocale(this, preferenceHelper.getLanguage())
        registerToken(preferenceHelper)

        viewModel.getAppSettings()
        binding.ivLoading.animateView()
        // set app settings


        viewModel.splashResponse.observe(this) {
            when {
                it.reLogin -> this.reLogin(preferenceHelper)
                it.error.isNotEmpty() -> this.reLogin(preferenceHelper)
                it.data != null -> {
                    when (it.flag) {
                        0 -> {
                            preferenceHelper.setAppSettings((it.data as SettingsResponse).settingsObject!!)
                            if (preferenceHelper.getUserProfile().apiToken.toString().isEmpty()) {
                                preferenceHelper.getUserProfile().apiToken.toString().let { api ->
                                    if (api.isEmpty() || api == "null")
                                        binding.clNewUser.visibility = View.VISIBLE
                                }
                            } else {
                                viewModel.getUserProfile()
                            }
                        }
                        1 -> {
                            (it.data as ProfileObject).also { profile ->
                                if (profile.isActive == "1") {
                                    crToast(getString(R.string.user_blocked))
                                } else {
                                    preferenceHelper.setUserProfile(profile).also {
                                        val bundle = intent.extras
                                        if (bundle?.getString("action") != null) {
                                            val notificationModel = NotificationsModels(action = bundle.getString("action"),
                                                    post_id = bundle.getString("post_id"))
                                            navigateToActivity(MainActivity::class.java, finish = true, data = notificationModel)
                                        } else {
                                            navigateToActivity(MainActivity::class.java, true)
                                        }
                                    }
                                }

                            }
                        }
                        2 -> {
                            navigateToActivity(MainActivity::class.java, true)
                        }
                        else -> this.reLogin(preferenceHelper)
                    }
                }

            }
        }

        // go to login
        binding.tvLogin.setOnClickListener {
            navigateToActivity(LoginActivity::class.java , true)
        }
        binding.tvBrowse.setOnClickListener {
            preferenceHelper.setGuestStatus(true).also {
                showProgress()
                viewModel.visitorRequest = VisitorRequest(deviceToken = preferenceHelper.getFCMToken())
                viewModel.loginAsVisitor()
            }
        }
    }
}

