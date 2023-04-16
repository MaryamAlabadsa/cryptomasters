package com.mas.cryptomasters.ui.othersActivity

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.SnapHelper
import com.mas.BaseActivity
import com.mas.cryptomasters.R
import com.mas.cryptomasters.adapters.ImageListAdapter
import com.mas.cryptomasters.data.response.home.PostsData
import com.mas.cryptomasters.databinding.ActivityPlayerBinding
import com.mas.cryptomasters.utils.Extensions.crToast
import com.mas.cryptomasters.utils.Extensions.generateVideoLink
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlayerActivity : BaseActivity<ActivityPlayerBinding>(ActivityPlayerBinding::inflate) {
    private lateinit var sliderAdapter: ImageListAdapter
        override fun setView(phoneIsConnected: Boolean) {

            binding.ivBack.setOnClickListener {
                finish()
            }
            if (!phoneIsConnected){
                crToast(getString(R.string.no_connection))
                return
            }


        val snapHelper: SnapHelper = PagerSnapHelper()
        sliderAdapter = ImageListAdapter()
        binding.rvImagesList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvImagesList.adapter = sliderAdapter
        snapHelper.attachToRecyclerView(binding.rvImagesList)

        if (!intent.getStringExtra("videoUrl").isNullOrEmpty()) {
            binding.videoView.setSource(intent.getStringExtra("videoUrl")!!.generateVideoLink())
            binding.rvImagesList.visibility = View.GONE
            binding.videoView.visibility = View.VISIBLE
        } else if (intent.getParcelableExtra<PostsData>("postData") != null) {
            val postsData = intent.getParcelableExtra<PostsData>("postData")

            binding.rvImagesList.visibility = View.VISIBLE
            binding.videoView.visibility = View.GONE
            sliderAdapter.updateListToEdit(postsData?.galleries!! , true)



        } else {
            crToast()
            finish()
        }

        binding.ivBack.setOnClickListener {
            finish()
        }

        if (preferenceHelper.getUserProfile().isPaid != "1") {
            setAds(binding.viewForAds)
        }

    }


}