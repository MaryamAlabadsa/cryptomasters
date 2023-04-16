package com.mas.cryptomasters.ui.fragment.profile

import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.mas.cryptomasters.BaseFragment
import com.mas.cryptomasters.adapters.PostsAdapter
import com.mas.cryptomasters.data.response.ProfileResponse
import com.mas.cryptomasters.databinding.FragmentProfileBinding
import com.mas.cryptomasters.databinding.FragmentYourPostsBinding
import com.mas.cryptomasters.ui.fragment.home.HomeFragmentViewModel
import com.mas.cryptomasters.ui.fragment.posts.PostsViewModel
import com.mas.cryptomasters.ui.othersActivity.NavigationViewModel
import com.mas.cryptomasters.utils.Extensions.crToast
import com.mas.cryptomasters.utils.Extensions.reLogin
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class YourPostsFragment : BaseFragment<FragmentYourPostsBinding>() {
    private val viewModel: PostsViewModel by viewModels()

    private lateinit var postsAdapter: PostsAdapter
    override fun getViewBinding() = FragmentYourPostsBinding.inflate(layoutInflater)

    override fun init() {

        viewModel.getProfile()

        postsAdapter = PostsAdapter(requireActivity(), viewModel, this, parentFragmentManager)
        binding.rvPost.layoutManager = LinearLayoutManager(requireContext())
        binding.rvPost.adapter = postsAdapter

        viewModel.profileMutable.observe(this) {
            when {
                it.reLogin -> requireActivity().reLogin(preferences)
                it.error.isNotEmpty() -> {
                    handleLoading(binding.loading, true)
                    requireContext().crToast()
                }
                it.data != null -> {
                    handleLoading(binding.loading, false)
                    postsAdapter.updateAdapter((it.data as ProfileResponse).profileObject!!.posts!!)
                }
            }
        }
    }


    override fun onResume() {
        super.onResume()
        // to check if there any updates
        preferences.getSomeUpdate().let {
            if (it.whereData != null) {
                postsAdapter.updateSinglePost(it.whereData)
            }
            preferences.clearUpdate()
        }

    }



}