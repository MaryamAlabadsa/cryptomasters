package com.mas.cryptomasters.ui.signup

import android.os.Bundle
import androidx.activity.viewModels
import com.mas.BaseActivity
import com.mas.cryptomasters.R
import com.mas.cryptomasters.data.request.RegisterRequest
import com.mas.cryptomasters.data.response.ProfileResponse
import com.mas.cryptomasters.databinding.ActivityRegisterBinding
import com.mas.cryptomasters.ui.main.MainActivity
import com.mas.cryptomasters.ui.othersActivity.NavigationActivity
import com.mas.cryptomasters.utils.Extensions.crToast
import com.mas.cryptomasters.utils.Extensions.hideProgress
import com.mas.cryptomasters.utils.Extensions.isInputsNotValid
import com.mas.cryptomasters.utils.Extensions.loadWebImage
import com.mas.cryptomasters.utils.Extensions.navigateToActivity
import com.mas.cryptomasters.utils.Extensions.registerToken
import com.mas.cryptomasters.utils.Extensions.setTextWithTowColor
import com.mas.cryptomasters.utils.Extensions.showProgress
import com.mas.cryptomasters.utils.Navigate
import com.mas.cryptomasters.utils.ToastType
import com.vansuita.pickimage.bean.PickResult
import com.vansuita.pickimage.bundle.PickSetup
import com.vansuita.pickimage.dialog.PickImageDialog
import com.vansuita.pickimage.listeners.IPickResult
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File


@AndroidEntryPoint
class RegisterActivity : BaseActivity<ActivityRegisterBinding>(ActivityRegisterBinding::inflate),
    IPickResult {
    private val viewModel: RegisterViewModel by viewModels()
    private var imagePath: String? = null

    private var avatar = ""
    override fun setView(phoneIsConnected: Boolean) {

        if (!phoneIsConnected){
            crToast(getString(R.string.no_connection))
            return
        }


        registerToken(preferenceHelper)

        setTextWithTowColor(
            binding.tvHaveAccount,
            string1 = getString(R.string.have_account1),
            string2 = getString(R.string.have_account2)
        )


        binding.tvRegister.setOnClickListener {
            binding.cpCode.registerCarrierNumberEditText(binding.txtPhone)

            if (isInputValid()) {
                showProgress()
                if (imagePath != null) {
                    uploadProfileImage(imagePath!!, "image".toMediaTypeOrNull())
                } else {
                    register()
                }
            }
        }
        binding.ivProfile.setOnClickListener {
            PickImageDialog.build(PickSetup()).show(this)
        }



        viewModel.registerResponse.observe(this) {
            hideProgress()
            when {
                it.reLogin -> crToast(getString(R.string.phone_is_used))
                it.error.isNotEmpty() -> crToast()
                it.data != null && it.flag == 1 -> {
                    crToast(getString(R.string.registration_successful), ToastType.SUCCESS)
                    preferenceHelper.setUserProfile((it.data as ProfileResponse).profileObject!!)
                    navigateToActivity(MainActivity::class.java, true)
                }
                it.data != null && it.flag == 0 -> {
                    avatar = "${it.data}"
                    register()
                }
            }
        }

        binding.tvTerms.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(Navigate.TARGET, Navigate.TERMS)
            navigateToActivity(NavigationActivity::class.java, bundle = bundle)
        }

        binding.tvHaveAccount.setOnClickListener {
            finish()
        }
    }

    private fun register() {
        viewModel.registerReq = RegisterRequest(
            avatar = avatar,
            name = binding.txtName.text.toString(),
            password = binding.txtPassword.text.toString(),
            phone = binding.cpCode.fullNumberWithPlus,
            deviceToken = preferenceHelper.getFCMToken()
        )
        viewModel.register()
    }

    private fun isInputValid(): Boolean {
        binding.txtName.error = null
        binding.txtPhone.error = null
        binding.txtPassword.error = null
        binding.txtConfirmPassword.error = null

        val name = binding.txtName.text.toString()
        val phone = binding.cpCode.fullNumberWithPlus
        val password = binding.txtPassword.text.toString()
        val confirmation = binding.txtConfirmPassword.text.toString()

        if (isInputsNotValid(listOf(name, phone, password, confirmation)) || password.length < 6) {
            if (name.isEmpty())
                binding.txtName.error = "*"
            if (phone.isEmpty())
                binding.txtPhone.error = "*"
            if (password.isEmpty())
                binding.txtPassword.error = "*"
            if (password.length < 6)
                crToast(getString(R.string.password_validation))
            if (confirmation.isEmpty())
                binding.txtConfirmPassword.error = "*"
            return false
        } else if (password != confirmation) {
            binding.txtPassword.error = getString(R.string.mismatches)
            binding.txtConfirmPassword.error = getString(R.string.mismatches)
            return false
        } else if (!binding.cpCode.isValidFullNumber) {
            binding.txtPhone.error = "*"
            return false
        } else if (!binding.cbTerms.isChecked) {
            crToast(getString(R.string.terms))
            return false
        } else {
            return true
        }
    }

    override fun onPickResult(r: PickResult?) {
        if (r!!.error != null) {
            return
        }

        binding.ivProfile.loadWebImage(r.path, res = true)
        imagePath = r.path
    }

    private fun uploadProfileImage(videoPath: String, type: MediaType?) {
        val file = File(videoPath)
        val requestFile =
            file.asRequestBody(type)
        val body: MultipartBody.Part =
            MultipartBody.Part.createFormData("image", file.name, requestFile)

        val descriptionString = "image"
        val description = descriptionString.toRequestBody(MultipartBody.FORM)

        viewModel.uploadImage(description, body)
    }


}