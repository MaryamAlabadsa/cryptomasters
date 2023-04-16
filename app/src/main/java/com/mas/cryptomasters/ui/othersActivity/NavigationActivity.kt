package com.mas.cryptomasters.ui.othersActivity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.mas.BaseActivity
import com.mas.cryptomasters.R
import com.mas.cryptomasters.data.response.home.Coin
import com.mas.cryptomasters.data.response.home.PostsData
import com.mas.cryptomasters.data.response.recommendations.RecommendData
import com.mas.cryptomasters.databinding.ActivityOthersBinding
import com.mas.cryptomasters.ui.fragment.block.BlockFragment
import com.mas.cryptomasters.ui.fragment.notifications.NotificationsFragment
import com.mas.cryptomasters.ui.fragment.others.ContactFragment
import com.mas.cryptomasters.ui.fragment.others.TermsFragment
import com.mas.cryptomasters.ui.fragment.posts.CreatePostFragment
import com.mas.cryptomasters.ui.fragment.posts.EditPostFragment
import com.mas.cryptomasters.ui.fragment.posts.PostDetailsFragment
import com.mas.cryptomasters.ui.fragment.profile.ProfileFragment
import com.mas.cryptomasters.ui.fragment.recommend.RecommendDetailsFragment
import com.mas.cryptomasters.ui.fragment.recommend.RecommendFragment
import com.mas.cryptomasters.ui.fragment.rules.CoinFragment
import com.mas.cryptomasters.utils.Extensions.askLogin
import com.mas.cryptomasters.utils.Extensions.crToast
import com.mas.cryptomasters.utils.Navigate
import com.mas.cryptomasters.utils.UpdateData
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class NavigationActivity : BaseActivity<ActivityOthersBinding>(ActivityOthersBinding::inflate) {
    var updateData: UpdateData? = null
    override fun setView(phoneIsConnected: Boolean) {

        binding.ivBack.setOnClickListener {
            finish()
        }

        if (!phoneIsConnected) {
            crToast(getString(R.string.no_connection))
            return
        }

        if (intent.hasExtra(Navigate.TARGET)) {
            when (intent.getStringExtra(Navigate.TARGET)) {
                Navigate.CONTACTS -> setFragment(ContactFragment())
                Navigate.REPORT -> {
                    val bundle = Bundle()
                    bundle.putString(Navigate.TARGET, Navigate.REPORT)
                    setFragmentWithBundle(ContactFragment(), bundle)
                }
                Navigate.PROFILE -> setFragment(ProfileFragment())
                Navigate.NOTIFICATION -> {
                    binding.ivNotifications.visibility = View.GONE
                    setFragment(NotificationsFragment())
                }
                Navigate.TERMS -> setFragment(TermsFragment())
                Navigate.CREATE_POST -> setFragment(CreatePostFragment())
                Navigate.POST_DETAILS -> {
                    val bundle = Bundle()
                    if (intent.hasExtra(Navigate.DATA_POST)) {
                        val currentPost = intent.getParcelableExtra<PostsData>(Navigate.DATA_POST)
                        bundle.putParcelable(Navigate.DATA_POST, currentPost)
                    } else if (intent.hasExtra(Navigate.DATA_ID)) {
                        val postId = intent.getStringExtra(Navigate.DATA_ID)
                        bundle.putString(Navigate.DATA_ID, postId)
                    }
                    setFragmentWithBundle(PostDetailsFragment(), bundle)
                }
                Navigate.EDIT_POST -> {
                    val bundle = Bundle()
                    val currentPost = intent.getParcelableExtra<PostsData>(Navigate.DATA_POST)
                    val activityName = intent.getStringExtra(Navigate.SOURCE)
                    bundle.putParcelable(Navigate.DATA_POST, currentPost)
                    bundle.putString(Navigate.SOURCE, activityName)
                    setFragmentWithBundle(EditPostFragment(), bundle)
                }
                Navigate.RECOMMEND_DATA -> {
                    val currentData = intent.getParcelableExtra<RecommendData>(Navigate.DATA_POST)
                    val bundle = Bundle()
                    bundle.putParcelable(Navigate.DATA_POST, currentData)
                    setFragmentWithBundle(RecommendDetailsFragment(), bundle)
                }
                Navigate.RECOMMEND_FRAGMENT -> {
                    val bundle = Bundle()
                    bundle.putString(Navigate.TARGET, (Navigate.RECOMMEND_FRAGMENT))
                    setFragmentWithBundle(RecommendFragment(), bundle)
                }
                Navigate.COIN_RULE -> {
                    val bundle = Bundle()
                    val currentData = intent.getParcelableExtra<Coin>(Navigate.COIN_DATA)
                    bundle.putParcelable(Navigate.COIN_DATA, currentData)
                    setFragmentWithBundle(CoinFragment(), bundle)
                }
                Navigate.BLOCK -> {
                    binding.tvTitle.text = getString(R.string.block_list)
                    setFragment(BlockFragment())
                }
            }
        }


        binding.ivNotifications.setOnClickListener {
            if (preferenceHelper.isGustUser()) {
                this.askLogin()
            } else {
                binding.ivNotifications.visibility = View.GONE
                setFragment(NotificationsFragment())
            }

        }


        if (preferenceHelper.getUserProfile().isPaid != "1") {
            setAds(binding.viewForAds)
        }
    }

    private fun setFragmentWithBundle(fragment: Fragment, bundle: Bundle? = null) {
        fragment.arguments = bundle
        setFragment(fragment)
    }


    private fun setFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(binding.clFragment.id, fragment).commit()
    }


}

