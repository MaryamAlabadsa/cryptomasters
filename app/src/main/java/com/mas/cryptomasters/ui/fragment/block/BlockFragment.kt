package com.mas.cryptomasters.ui.fragment.block

import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.mas.cryptomasters.BaseFragment
import com.mas.cryptomasters.R
import com.mas.cryptomasters.adapters.BlockAdapter
import com.mas.cryptomasters.data.response.block.BlockData
import com.mas.cryptomasters.data.response.block.BlockResponse
import com.mas.cryptomasters.databinding.FragmentBlockBinding
import com.mas.cryptomasters.utils.Extensions.crToast
import com.mas.cryptomasters.utils.Extensions.hideProgress
import com.mas.cryptomasters.utils.Extensions.reLogin
import com.mas.cryptomasters.utils.Extensions.showProgress
import com.mas.cryptomasters.utils.ToastType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BlockFragment : BaseFragment<FragmentBlockBinding>() {

    private val viewModel: BlockViewModels by viewModels()
    private lateinit var blockListAdapter: BlockAdapter

    override fun getViewBinding() = FragmentBlockBinding.inflate(layoutInflater)

    override fun init() {

        blockListAdapter = BlockAdapter()
        binding.rvList.layoutManager = LinearLayoutManager(requireContext())
        binding.rvList.adapter = blockListAdapter

        blockListAdapter.mutableUser.observe(this) {
            if (it != null) {
                requireActivity().showProgress()
                viewModel.unBlockUser(it)
                blockListAdapter.mutableUser.postValue(null)
            }
        }



        viewModel.responseLiveData.observe(this) {
            when {
                it.reLogin -> requireActivity().reLogin(preferences)
                it.error.isNotEmpty() -> {
                    hideProgress()
                    handleLoading(binding.loading, true)
                    requireContext().crToast()
                }
                // block list
                it.data != null && it.flag == 0 -> {
                    handleLoading(binding.loading, false)
                    if ((it.data as BlockResponse).data.isEmpty()) {
                        handleLoading(binding.loading, true)
                        binding.loading.tvErrorMessage.text = getString(R.string.no_items)
                    } else {
                        blockListAdapter.updateAdapter((it.data as BlockResponse).data)
                    }
                }
                // unblock
                it.data != null && it.flag == 1 -> {
                    hideProgress()
                    handleLoading(binding.loading, false)
                    requireContext().crToast(getString(R.string.un_block_success) , ToastType.SUCCESS)
                    blockListAdapter.deleteItem(it.data as BlockData)

                }
            }
        }

    }

}