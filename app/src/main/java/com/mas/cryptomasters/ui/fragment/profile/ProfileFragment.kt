package com.mas.cryptomasters.ui.fragment.profile

import android.Manifest
import android.net.Uri
import androidx.fragment.app.viewModels
import com.mas.cryptomasters.BaseFragment
import com.mas.cryptomasters.R
import com.mas.cryptomasters.data.request.EditProfileRequest
import com.mas.cryptomasters.data.response.DefaultResponse
import com.mas.cryptomasters.data.response.ProfileResponse
import com.mas.cryptomasters.databinding.FragmentEditProfileBinding
import com.mas.cryptomasters.databinding.FragmentProfileBinding
import com.mas.cryptomasters.ui.othersActivity.NavigationViewModel
import com.mas.cryptomasters.utils.Extensions
import com.mas.cryptomasters.utils.Extensions.crToast
import com.mas.cryptomasters.utils.Extensions.imageToBase64
import com.mas.cryptomasters.utils.Extensions.loadWebImage
import com.mas.cryptomasters.utils.Extensions.permissionRequest
import com.mas.cryptomasters.utils.Extensions.reLogin
import com.mas.cryptomasters.utils.Extensions.showProgress
import com.mas.cryptomasters.utils.PermissionsAction
import com.mas.cryptomasters.utils.ToastType
import com.vansuita.pickimage.bundle.PickSetup
import com.vansuita.pickimage.dialog.PickImageDialog
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File


@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>() {

    private lateinit var appPagerAdapter: AppPagerAdapter
    val viewModel: ProfileViewModel by viewModels()
    override fun getViewBinding() = FragmentProfileBinding.inflate(layoutInflater)


    override fun init() {
        setUserInfo()
        setPagesAdapter()

        binding.tvEditImage.setOnClickListener {
            requireContext().permissionRequest(listOf(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ), object : PermissionsAction {
                override fun onGrant() {
                    PickImageDialog.build(PickSetup())
                        .setOnPickResult {
                            requireActivity().showProgress()
                            binding.ivImage.loadWebImage(it.path + "", res = true)
                            uploadProfileImage(it.path, "image".toMediaTypeOrNull())
                        }
                        .setOnPickCancel {
                        }.show(requireActivity().supportFragmentManager)
                }

                override fun onDined() {
                    requireContext().crToast(getString(R.string.permissions_required))
                }
            })
        }

        viewModel.mutableProfileEdit.observe(this) {
            Extensions.hideProgress()
            when {
                it.reLogin -> requireActivity().reLogin(preferences)
                it.error.isNotEmpty() -> requireActivity().crToast()
                it.data != null && it.flag == 1 -> {
                    preferences.setUserProfile((it.data as ProfileResponse).profileObject!!)
                    binding.tvName.text = preferences.getUserProfile().name
                    requireActivity().crToast(
                        requireActivity().getString(R.string.update_profile),
                        ToastType.SUCCESS
                    )
                }
                it.data != null && it.flag == 0 -> {
                    viewModel.editProfileRequest = EditProfileRequest(
                        name = preferences.getUserProfile().name.toString(),
                        avatar = it.data.toString()
                    )
                    viewModel.updateProfile()
                }
            }
        }

    }

    private fun setUserInfo() {
        preferences.getUserProfile()
            .let {
                binding.ivImage.loadWebImage(it.avatar + "")
                binding.tvName.text = it.name
                binding.tvPhone.text = it.phone
            }
    }

    private fun setPagesAdapter() {
        appPagerAdapter = AppPagerAdapter(requireActivity().supportFragmentManager)
        appPagerAdapter.addFragment(YourPostsFragment(), getString(R.string.your_posts))
        appPagerAdapter.addFragment(
            EditProfileFragment(viewModel),
            getString(R.string.edit_profile)
        )
        appPagerAdapter.addFragment(ChangePasswordFragment(), getString(R.string.change_password))
        binding.tabLayout.setupWithViewPager(binding.pager)
        binding.pager.adapter = appPagerAdapter
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