package com.mas.cryptomasters.ui.fragment.others

import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.fragment.app.viewModels
import com.mas.cryptomasters.BaseFragment
import com.mas.cryptomasters.R
import com.mas.cryptomasters.data.request.ContactRequest
import com.mas.cryptomasters.databinding.FragmentContactBinding
import com.mas.cryptomasters.databinding.FragmentNotificationsBinding
import com.mas.cryptomasters.ui.main.MainActivity
import com.mas.cryptomasters.ui.othersActivity.NavigationViewModel
import com.mas.cryptomasters.utils.Constants
import com.mas.cryptomasters.utils.Extensions.crToast
import com.mas.cryptomasters.utils.Extensions.hideProgress
import com.mas.cryptomasters.utils.Extensions.isInputsNotValid
import com.mas.cryptomasters.utils.Extensions.navigateToActivity
import com.mas.cryptomasters.utils.Extensions.reLogin
import com.mas.cryptomasters.utils.Extensions.setInputErrorHint
import com.mas.cryptomasters.utils.Extensions.showProgress
import com.mas.cryptomasters.utils.Navigate
import com.mas.cryptomasters.utils.ToastType
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ContactFragment : BaseFragment<FragmentContactBinding>() {

    override fun getViewBinding() = FragmentContactBinding.inflate(layoutInflater)

    private val viewModel: NavigationViewModel by viewModels()
    private lateinit var name: String
    private lateinit var phone: String
    override fun init() {

        if (preferences.isGustUser()) {
            binding.linerPhone.visibility = View.VISIBLE
            binding.txtName.visibility = View.VISIBLE
        } else {
            binding.txtName.visibility = View.GONE
            binding.linerPhone.visibility = View.GONE
        }

        if (arguments?.containsKey(Navigate.TARGET) == true) {
            if (requireArguments().getString(Navigate.TARGET) == Navigate.REPORT) {
                binding.txtTitle.setText(getString(R.string.report))
                binding.clSpinner.visibility = View.VISIBLE
                binding.tvTitleMessage.text = getText(R.string.report_item)
                binding.txtTitle.isEnabled = false
            }
        }

        binding.tvSend.setOnClickListener {
            val messageTitle = binding.txtTitle.text.toString()
            val message = binding.txtMessage.text.toString()

            if (!preferences.isGustUser()) {
                name = preferences.getUserProfile().name.toString()
                phone = preferences.getUserProfile().phone.toString()
            } else {
                binding.cpCode.registerCarrierNumberEditText(binding.txtPhone)
                name = binding.txtName.text.toString()
                phone = binding.cpCode.fullNumberWithPlus
                if (!binding.cpCode.isValidFullNumber) {
                    binding.txtPhone.error = "*"
                    return@setOnClickListener
                }
            }
            if (requireContext().isInputsNotValid(listOf(name, messageTitle, message, phone))) {
                requireContext().setInputErrorHint(
                    listOf(
                        binding.txtName,
                        binding.txtTitle,
                        binding.txtMessage,
                        binding.txtPhone
                    )
                )
            } else {
                requireActivity().showProgress()
                viewModel.contactRequest = ContactRequest(
                    name = name,
                    phone = phone,
                    subject = messageTitle,
                    text = message,
                )
                viewModel.contact()
            }
        }


        binding.ivTelgram.setOnClickListener {
            if (preferences.getAppSettings().tg != null && preferences.getAppSettings().tg == "null") {
                return@setOnClickListener
            }

            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(Constants.telegramLink + preferences.getAppSettings().tg)
                )
            )
        }
        binding.ivWhatsApp.setOnClickListener {
            if (preferences.getAppSettings().whts != null && preferences.getAppSettings().whts == "null") {
                return@setOnClickListener
            }
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(Constants.whatsappLink + preferences.getAppSettings().whts)
                )
            )
        }

        // observe here
        viewModel.contactResponse.observe(this) {
            hideProgress()
            when {
                it.reLogin -> requireActivity().reLogin(preferences)
                it.error.isNotEmpty() -> requireContext().crToast()
                it.data != null -> {
                    requireContext().crToast(getString(R.string.send_success), ToastType.SUCCESS)
                    requireActivity().navigateToActivity(MainActivity::class.java, true)
                }
            }
        }
    }

}