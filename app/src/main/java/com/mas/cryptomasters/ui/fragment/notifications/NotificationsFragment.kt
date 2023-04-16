package com.mas.cryptomasters.ui.fragment.notifications

import android.util.Log
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mas.cryptomasters.BaseFragment
import com.mas.cryptomasters.R
import com.mas.cryptomasters.adapters.NotificationsAdapter
import com.mas.cryptomasters.data.response.notifications.NotificationsData
import com.mas.cryptomasters.data.response.notifications.NotificationsResponse
import com.mas.cryptomasters.databinding.FragmentHomeBinding
import com.mas.cryptomasters.databinding.FragmentNotificationsBinding
import com.mas.cryptomasters.utils.Extensions.askLogin
import com.mas.cryptomasters.utils.Extensions.crToast
import com.mas.cryptomasters.utils.Extensions.hideProgress
import com.mas.cryptomasters.utils.Extensions.reLogin
import com.mas.cryptomasters.utils.Extensions.showProgress
import com.mas.cryptomasters.utils.RESPONSE.*
import com.mas.cryptomasters.utils.SwipeToDeleteCallback
import com.mas.cryptomasters.utils.ToastType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationsFragment : BaseFragment<FragmentNotificationsBinding>() {

    private lateinit var notificationsAdapter: NotificationsAdapter
    private val viewModel: NotificationsViewModel by viewModels()

    override fun getViewBinding() = FragmentNotificationsBinding.inflate(layoutInflater)

    override fun init() {

        if (preferences.isGustUser()) {
            activity?.askLogin()
            return
        }

        notificationsAdapter = NotificationsAdapter(preferences, requireActivity())
        binding.rvList.layoutManager = LinearLayoutManager(requireActivity())
        binding.rvList.adapter = notificationsAdapter

        viewModel.mutableNotifications.observe(this) {
            when {
                it.reLogin -> requireActivity().reLogin(preferences)
                it.error.isNotEmpty() -> {
                    handleLoading(binding.loading, true)
                    requireActivity().crToast()
                }
                it.data != null && it.flag == 1 -> {
                    notificationsAdapter.updateAdapter((it.data as NotificationsResponse).data)
                    handleLoading(binding.loading, false)
                }
                it.data != null && it.flag == -1 -> {
                    hideProgress()
                    notificationsAdapter.removeNotification(it.data as NotificationsData)
                    requireActivity().crToast(getString(R.string.post_delete_success) , ToastType.INFO)
                }
            }
        }
        enableSwipeToDeleteAndUndo()
    }

    private fun enableSwipeToDeleteAndUndo() {
        val swipeToDeleteCallback: SwipeToDeleteCallback =
            object : SwipeToDeleteCallback(requireContext()) {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, i: Int) {
                    notificationsAdapter.getNotification(viewHolder.bindingAdapterPosition)
                        .let {
                            requireActivity().showProgress()
                            viewModel.deleteNotifications(it)
                        }
                }
            }
        val itemTouchhelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchhelper.attachToRecyclerView(binding.rvList)
    }
}