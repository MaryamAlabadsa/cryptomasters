package com.mas.cryptomasters.ui.forgetPassword

import androidx.activity.viewModels
import com.mas.BaseActivity
import com.mas.cryptomasters.R
import com.mas.cryptomasters.data.request.ResetPasswordRequest
import com.mas.cryptomasters.databinding.ActivityResetPasswordBinding
import com.mas.cryptomasters.ui.login.LoginActivity
import com.mas.cryptomasters.utils.Extensions.crToast
import com.mas.cryptomasters.utils.Extensions.hideProgress
import com.mas.cryptomasters.utils.Extensions.showProgress
import com.mas.cryptomasters.utils.Navigate
import com.mas.cryptomasters.utils.RESPONSE
import com.mas.cryptomasters.utils.ToastType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResetPasswordActivity :
    BaseActivity<ActivityResetPasswordBinding>(ActivityResetPasswordBinding::inflate) {
    private val viewModel: NewPasswordViewModel by viewModels()
    private var userPhone: String? = null

    override fun setView(phoneIsConnected: Boolean) {
        if (!phoneIsConnected){
            crToast(getString(R.string.no_connection))
            binding.btnConfirm.isEnabled = false
            return
        }

        userPhone = intent.getStringExtra(Navigate.USER_PHONE)!!
        binding.btnConfirm.setOnClickListener {
            val password = binding.txtNewPassword.text.toString()
            val passwordConfirm = binding.txtConfirmPassword.text.toString()

            if (userPhone != null && password.length >= 8 && password == passwordConfirm) {
                showProgress()
                viewModel.resetPasswordRequest = ResetPasswordRequest(
                    passwordConfirmation = passwordConfirm,
                    password = password,
                    phone = userPhone
                )
                viewModel.reset()
            } else {
                if (password.length < 8)
                    binding.txtNewPassword.error = "!"

                if (passwordConfirm != password)
                    binding.txtConfirmPassword.error = "!"

            }

        }

        // observe
        viewModel.passwordRestMutable.observe(this) {
            hideProgress()
            when (it!!) {
                RESPONSE.SUCCESS -> {
                    crToast(getString(R.string.reset_success), ToastType.SUCCESS)
                    navigateToActivity(LoginActivity::class.java, finish = true)
                }
                RESPONSE.ERROR -> crToast()
                RESPONSE.AUT -> crToast()
            }
        }
    }

}
