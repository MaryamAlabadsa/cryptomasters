package com.mas.cryptomasters.ui.fragment.profile

import androidx.fragment.app.viewModels
import com.mas.cryptomasters.BaseFragment
import com.mas.cryptomasters.R
import com.mas.cryptomasters.data.request.ChangePasswordRequest
import com.mas.cryptomasters.databinding.FragmentChangePasswordBinding
import com.mas.cryptomasters.databinding.FragmentPostDetailsBinding
import com.mas.cryptomasters.ui.main.MainActivity
import com.mas.cryptomasters.ui.othersActivity.NavigationViewModel
import com.mas.cryptomasters.utils.Constants
import com.mas.cryptomasters.utils.Extensions
import com.mas.cryptomasters.utils.Extensions.crToast
import com.mas.cryptomasters.utils.Extensions.navigateToActivity
import com.mas.cryptomasters.utils.Extensions.reLogin
import com.mas.cryptomasters.utils.Extensions.showProgress
import com.mas.cryptomasters.utils.ToastType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangePasswordFragment : BaseFragment<FragmentChangePasswordBinding>() {

    private val viewModel: NavigationViewModel by viewModels()
    override fun getViewBinding() = FragmentChangePasswordBinding.inflate(layoutInflater)

    override fun init() {

        binding.btnConfirm.setOnClickListener {
            val oldPassword = binding.txtOldPassword.text.toString()
            val newPassword = binding.txtNewPassword.text.toString()
            val confirmPassword = binding.txtConfirmPassword.text.toString()
            if (newPassword.length >= Constants.PASSWORD_LENGTH && confirmPassword.length >= Constants.PASSWORD_LENGTH && (newPassword == confirmPassword)) {
                requireActivity().showProgress()

                viewModel.changePasswordRequest = ChangePasswordRequest(
                    oldPassword = oldPassword,
                    password = newPassword,
                    passwordConfirmation = confirmPassword
                )

                viewModel.changePassword()
            } else {
                if (newPassword.length < Constants.PASSWORD_LENGTH)
                    binding.txtNewPassword.error = getString(R.string.password_validation)
                if (confirmPassword.length < Constants.PASSWORD_LENGTH)
                    binding.txtConfirmPassword.error = getString(R.string.password_validation)
                if (newPassword != confirmPassword)
                    binding.txtConfirmPassword.error = getString(R.string.not_match)
            }
        }
        // observe
        viewModel.changePasswordResponse.observe(this) {
            Extensions.hideProgress()
            when {
                it.reLogin -> requireActivity().reLogin(preferences)
                it.error.isNotEmpty() -> requireContext().crToast(getString(R.string.check_rest_data))
                it.data != null -> {
                    binding.txtOldPassword.setText("")
                    binding.txtConfirmPassword.setText("")
                    binding.txtNewPassword.setText("")

                    requireContext().crToast(getString(R.string.send_success), ToastType.SUCCESS)
                }
            }
        }
    }


}