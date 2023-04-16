package com.mas.cryptomasters.ui.fragment.settings


import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.mas.cryptomasters.BaseFragment
import com.mas.cryptomasters.R
import com.mas.cryptomasters.databinding.FragmentCoinBinding
import com.mas.cryptomasters.databinding.FragmentSettingsBinding
import com.mas.cryptomasters.ui.dialogs.DeleteAdsDialog
import com.mas.cryptomasters.ui.dialogs.LanguageDialog
import com.mas.cryptomasters.ui.fragment.home.HomeFragmentViewModel
import com.mas.cryptomasters.ui.fragment.profile.ProfileFragment
import com.mas.cryptomasters.ui.login.LoginActivity
import com.mas.cryptomasters.ui.othersActivity.NavigationActivity
import com.mas.cryptomasters.utils.AlertAction
import com.mas.cryptomasters.utils.Constants
import com.mas.cryptomasters.utils.Extensions.navigateToActivity
import com.mas.cryptomasters.utils.Extensions.reLogin
import com.mas.cryptomasters.utils.Extensions.showAlert
import com.mas.cryptomasters.utils.Navigate
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : BaseFragment<FragmentSettingsBinding>() {
    private val viewModel: HomeFragmentViewModel by viewModels()

    private var bundle: Bundle = Bundle()
    private var languageDialog: LanguageDialog? = null
    private lateinit var deleteAdsDialog: DeleteAdsDialog

    override fun getViewBinding() = FragmentSettingsBinding.inflate(layoutInflater)

    override fun init() {

        deleteAdsDialog = DeleteAdsDialog(requireContext(), preferences)

        if (preferences.isGustUser()) {
            binding.tvProfile.visibility = View.GONE
            binding.tvBlockList.visibility = View.GONE

            binding.tvAds.visibility = View.GONE
            binding.tvLogout.text = getString(R.string.login)
        }


        if (preferences.getAppSettings().isHide == "0") {
            binding.tvAds.visibility = View.GONE
        }


        binding.tvProfile.setOnClickListener {
            bundle.putString(Navigate.TARGET, Navigate.PROFILE)
            requireActivity().navigateToActivity(
                NavigationActivity::class.java,
                bundle = bundle
            )

        }

        // contact us
        binding.tvContact.setOnClickListener {
            bundle.putString(Navigate.TARGET, Navigate.CONTACTS)
            requireActivity().navigateToActivity(NavigationActivity::class.java, bundle = bundle)
        }

        //terms
        binding.tvTerms.setOnClickListener {
            bundle.putString(Navigate.TARGET, Navigate.TERMS)
            requireActivity().navigateToActivity(NavigationActivity::class.java, bundle = bundle)
        }

        binding.tvBlockList.setOnClickListener {
            bundle.putString(Navigate.TARGET, Navigate.BLOCK)
            requireActivity().navigateToActivity(NavigationActivity::class.java, bundle = bundle)

        }

        //logout
        binding.tvLogout.setOnClickListener {
            if (!preferences.isGustUser()) {
                requireContext().showAlert(
                    title = getString(R.string.log_out),
                    action = object : AlertAction {
                        override fun onConfirm() {
                            preferences.getUserProfile()
                            requireActivity().reLogin(preferences)
                        }
                    }
                )
            } else {
                requireActivity().navigateToActivity(LoginActivity::class.java, finish = true)
            }

        }

        //delete ads
        binding.tvAds.setOnClickListener {
            deleteAdsDialog.show()
        }

        //language
        binding.tvLanguage.setOnClickListener {
            languageDialog = LanguageDialog(requireContext(), preferences)
                .setAction(object : AlertAction {
                    override fun onConfirm() {
                        requireActivity().finish()
                        startActivity(requireActivity().intent)
                    }
                })
            languageDialog!!.show()
        }

        binding.ivNotifications.setOnClickListener {
            navigateToNotifications(requireActivity())
        }

    }

    override fun onPause() {
        super.onPause()
        if (deleteAdsDialog.isShowing)
            deleteAdsDialog.dismiss()
    }

}