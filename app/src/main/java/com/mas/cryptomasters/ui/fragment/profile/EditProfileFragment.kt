package com.mas.cryptomasters.ui.fragment.profile

import com.mas.cryptomasters.BaseFragment
import com.mas.cryptomasters.data.request.EditProfileRequest
import com.mas.cryptomasters.databinding.FragmentChangePasswordBinding
import com.mas.cryptomasters.databinding.FragmentEditProfileBinding
import com.mas.cryptomasters.utils.Extensions.showProgress


class EditProfileFragment(val viewModel: ProfileViewModel) :
    BaseFragment<FragmentEditProfileBinding>() {
    override fun getViewBinding() = FragmentEditProfileBinding.inflate(layoutInflater)

    override fun init() {
        setCurrentData()

        binding.tvSave.setOnClickListener {
            if (binding.txtName.text.toString().trim().isNotEmpty()) {
                requireActivity().showProgress()
                viewModel.editProfileRequest = EditProfileRequest(
                    name = binding.txtName.text.toString(),
                    avatar = preferences.getUserProfile().avatar + ""
                )
                viewModel.updateProfile()
            } else {
                binding.txtName.error = "*"
            }
        }
    }

    private fun setCurrentData() {
        preferences.getUserProfile().let {
            binding.txtName.setText(it.name)
            binding.txtPhone.setText(it.phone)
            binding.txtPhone.isEnabled = false
        }


    }
}