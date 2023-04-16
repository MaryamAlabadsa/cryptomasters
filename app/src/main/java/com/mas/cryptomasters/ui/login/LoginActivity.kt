package com.mas.cryptomasters.ui.login

import android.os.Bundle
import androidx.activity.viewModels
import com.mas.BaseActivity
import com.mas.cryptomasters.R
import com.mas.cryptomasters.data.request.LoginRequest
import com.mas.cryptomasters.data.response.ProfileResponse
import com.mas.cryptomasters.databinding.ActivityLoginBinding
import com.mas.cryptomasters.ui.forgetPassword.ForgetPasswordActivity
import com.mas.cryptomasters.ui.main.MainActivity
import com.mas.cryptomasters.ui.othersActivity.NavigationActivity
import com.mas.cryptomasters.ui.signup.RegisterActivity
import com.mas.cryptomasters.utils.Extensions.crToast
import com.mas.cryptomasters.utils.Extensions.hideProgress
import com.mas.cryptomasters.utils.Extensions.navigateToActivity
import com.mas.cryptomasters.utils.Extensions.registerToken
import com.mas.cryptomasters.utils.Extensions.setTextWithTowColor
import com.mas.cryptomasters.utils.Extensions.showProgress
import com.mas.cryptomasters.utils.LocaleHelper
import com.mas.cryptomasters.utils.Navigate
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding>(ActivityLoginBinding::inflate) {
    private val viewModel: LoginViewModel by viewModels()
    override fun setView(phoneIsConnected: Boolean) {
        if (!phoneIsConnected){
            crToast(getString(R.string.no_connection))
            binding.tvLogin.isEnabled = false
            return
        }

        setTextWithTowColor(binding.tvSingUp)

        registerToken(preferenceHelper)

        binding.tvLogin.setOnClickListener {
            binding.txtPassword.error = null
            binding.txtPhone.error = null
            val password = binding.txtPassword.text.toString()
            // start login ===>
            binding.cpCode.registerCarrierNumberEditText(binding.txtPhone)
            val phone = binding.cpCode.fullNumberWithPlus

            if (binding.cpCode.isValidFullNumber && password.length >= 6) {
                showProgress()
                viewModel.loginRequest = LoginRequest(
                    deviceToken = preferenceHelper.getFCMToken(),
                    password = password,
                    phone = phone
                )
                viewModel.startLogin()
            } else {
                if (password.length < 6) {
                    binding.txtPassword.error = "*"
                    crToast(getString(R.string.password_validation))
                }

                if (!binding.cpCode.isValidFullNumber)
                    binding.txtPhone.error = "*"
            }
        }

        viewModel.profile.observe(this) {
            when{
                it.data !=null -> {
                    preferenceHelper.setUserProfile((it.data as ProfileResponse).profileObject!!)
                        .apply {
                            preferenceHelper.setGuestStatus(false).also {
                                hideProgress()
                                navigateToActivity(MainActivity::class.java, true)
                            }
                        }
                }
                it.reLogin ->{
                    crToast(getString(R.string.check_data))
                    hideProgress()
                }
                it.error.isNotEmpty() -> {
                    crToast()
                    hideProgress()
                }
            }

        }

        binding.tvSingUp.setOnClickListener {
            navigateToActivity(RegisterActivity::class.java)
        }

        binding.tvForgetPassword.setOnClickListener {
            navigateToActivity(ForgetPasswordActivity::class.java)
        }

        binding.tvTerms.setOnClickListener{
            val bundle = Bundle()
            bundle.putString(Navigate.TARGET, Navigate.TERMS)
            navigateToActivity(NavigationActivity::class.java, bundle = bundle)
        }
    }

}