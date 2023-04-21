package com.mas.cryptomasters.ui.fragment.posts

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.mas.cryptomasters.BaseFragment
import com.mas.cryptomasters.ChatActivity
import com.mas.cryptomasters.R
import com.mas.cryptomasters.adapters.ImageListAdapter
import com.mas.cryptomasters.data.request.CreatePostRequest
import com.mas.cryptomasters.databinding.FragmentCreatePostBinding
import com.mas.cryptomasters.ui.main.MainActivity
import com.mas.cryptomasters.utils.*
import com.mas.cryptomasters.utils.Extensions.crToast
import com.mas.cryptomasters.utils.Extensions.getBase64Image
import com.mas.cryptomasters.utils.Extensions.hideProgress
import com.mas.cryptomasters.utils.Extensions.loadWebImage
import com.mas.cryptomasters.utils.Extensions.navigateToActivity
import com.mas.cryptomasters.utils.Extensions.permissionRequest
import com.mas.cryptomasters.utils.Extensions.reLogin
import com.mas.cryptomasters.utils.Extensions.setInputErrorHint
import com.mas.cryptomasters.utils.Extensions.showProgress
import dagger.hilt.android.AndroidEntryPoint
import es.dmoral.toasty.Toasty.warning
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.util.logging.Logger

@AndroidEntryPoint
class CreatePostFragment : BaseFragment<FragmentCreatePostBinding>() {
    override fun getViewBinding() = FragmentCreatePostBinding.inflate(layoutInflater)
    private val viewModel: PostsViewModel by viewModels()

    private var resultLauncher: ActivityResultLauncher<Intent>? = null
    private var intentCode = -1

    private var videoPath: String? = null
    private var videoUri: Uri? = null

    private var imagesPath = ArrayList<String>()
    private var imageBase64List = ArrayList<String>()
    private lateinit var imageListAdapter: ImageListAdapter

    private var mInterstitialAd: InterstitialAd? = null
    private val adRequest: AdRequest = AdRequest.Builder().build()

    private val postRequest = CreatePostRequest()

    override fun init() {
        if (mInterstitialAd != null) {
            mInterstitialAd?.show(requireActivity())
            mInterstitialAd?.fullScreenContentCallback =
                object : FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() {
//                        intentToRule()
                    }
                    override fun onAdFailedToShowFullScreenContent(adError: AdError) {
//                        intentToRule()
                    }
                }
        }
        initImagesAdapter()

        binding.tvAddImage.setOnClickListener {
            requireContext().permissionRequest(listOf(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ),
                object : PermissionsAction {
                    override fun onGrant() {
                        intentCode = 1
                        val intent = Intent()
                        intent.type = "image/*"
                        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                        intent.action = Intent.ACTION_GET_CONTENT
                        resultLauncher!!.launch(
                            Intent.createChooser(
                                intent,
                                getString(R.string.select_images)
                            )
                        )
                    }

                    override fun onDined() {
                        requireContext().crToast(getString(R.string.permissions_required))
                    }
                })
        }
        binding.tvAddVideo.setOnClickListener {
            requireContext().permissionRequest(listOf(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ),
                object : PermissionsAction {
                    override fun onGrant() {
                        intentCode = 2

                        val intent = Intent()
                        intent.type = "*/*"
                        intent.putExtra(Intent.EXTRA_MIME_TYPES, arrayOf( "video/mp4"))

                        intent.action = Intent.ACTION_GET_CONTENT
                        resultLauncher!!.launch(intent)
                    }

                    override fun onDined() {
                        requireContext().crToast(getString(R.string.permissions_required))
                        return
                    }
                })
        }

        // get video or images
        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data: Intent? = result.data
                    if (intentCode == 1) {
                        handleGetImages(result)
                    } else {
                        handleGetVideo(data)
                    }
                }
            }

        //send the post
        binding.tvSend.setOnClickListener {
            if (!requireContext().setInputErrorHint(listOf(binding.txtText, binding.txtTitle))) {
                postRequest.title = binding.txtTitle.text.toString()
                postRequest.content = binding.txtText.text.toString()
                requireActivity().showProgress()
                createNewPost()
            }
        }

        // result observer
        viewModel.postStatusMutable.observe(this) {
            when {
                it.reLogin -> {
                    hideProgress()
                    requireActivity().reLogin(preferences)
                }
                it.error.isNotEmpty() -> {
                    hideProgress()
                    requireActivity().crToast()
                }
                // when video uploaded
                it.data != null && it.flag == 0 -> {
                    postRequest.video = it.data.toString()
                    viewModel.postRequest = postRequest
                    viewModel.createPost()
                }
                // new post create
                it.data != null && it.flag == 200 -> {
                    requireActivity().crToast(getString(R.string.post_success), ToastType.SUCCESS)
                    requireActivity().navigateToActivity(MainActivity::class.java , finish = true)
                }
            }
        }
        binding.ivDeleteFile.setOnClickListener {
            videoPath = null
            videoUri = null
            showHideVideoView(View.GONE)
        }

        imageListAdapter.mutableEmptyArray.observe(this) { size ->
            if (size <= 0) {
                imageListAdapter.mutableEmptyArray.postValue(1)
                showHidePhotosView(View.GONE)
                showHideVideoView(View.GONE)
            }
        }
    }

    private fun createNewPost() {
        if (videoPath != null && videoUri != null) {
            uploadVideo()
        } else {
            if (imageListAdapter.data.size > 0) {
                imageBase64List.clear()
                for (image in imageListAdapter.data) {
                    imageBase64List.add(requireContext().getBase64Image(image))
                }
                postRequest.images = imageBase64List
            }
            viewModel.postRequest = postRequest
            viewModel.createPost()
        }
    }

    private fun handleGetVideo(data: Intent?) {
        showHidePhotosView(View.GONE)
        showHideVideoView(View.VISIBLE)
        imageListAdapter.data.clear()

        videoPath = FileUtils.getPath(requireContext(), data!!.data!!).toString()
        videoUri = data.data!!
        binding.ivVideo.loadWebImage(url = videoPath!!, isProfile = false, res = true)
    }

    private fun handleGetImages(result: ActivityResult) {
        showHideVideoView(View.GONE)
        videoPath = null
        videoUri = null

        imagesPath.clear()
        if (result.data!!.clipData != null) {
            val count: Int = result.data!!.clipData!!.itemCount
            for (i in 0 until count) {
                val imageUri: Uri = result.data!!.clipData!!.getItemAt(i).uri
                imagesPath.add("$imageUri")
            }
        } else if (result.data!!.data != null) {
            val selectedImageUri: Uri = result.data?.data!!
            imagesPath.add("$selectedImageUri")
        }
        if (imagesPath.size > 0) {
            showHidePhotosView(View.VISIBLE)
            imageListAdapter.updateList(imagesPath)
        } else {
            showHidePhotosView(View.GONE)
        }
    }

    private fun showHidePhotosView(status: Int) {
        binding.clMedia.visibility = status
        binding.rvImagesList.visibility = status
    }

    private fun showHideVideoView(status: Int) {
        binding.clMedia.visibility = status
        binding.clVideo.visibility = status
    }

    private fun initImagesAdapter() {
        imageListAdapter = ImageListAdapter()
        binding.rvImagesList.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvImagesList.adapter = imageListAdapter
    }

    private fun uploadVideo() {
        val file = File(videoPath!!)
        val requestFile =
            file.asRequestBody(
                requireActivity().contentResolver.getType(videoUri!!)!!.toMediaTypeOrNull()
            )
        val body: MultipartBody.Part =
            MultipartBody.Part.createFormData("video", file.name, requestFile)

        val descriptionString = "video"
        val description = descriptionString.toRequestBody(MultipartBody.FORM)
        viewModel.uploadVideo(description, body)

    }
    private fun setInternalAds() {
        val Log = Logger.getLogger(ChatActivity::class.java.name)
        Log.warning("Hello World")
        InterstitialAd.load(context, Constants.INTERNAL_ADS, adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    mInterstitialAd = null
                    Log.warning("Hello World1212")

                }
                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    mInterstitialAd = interstitialAd
                    Log.warning("Hello World6666666")

                }
            })

    }

    override fun onResume() {
        super.onResume()
        setInternalAds()
    }
}