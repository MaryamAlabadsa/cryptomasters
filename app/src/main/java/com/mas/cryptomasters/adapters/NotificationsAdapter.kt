package com.mas.cryptomasters.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mas.cryptomasters.R
import com.mas.cryptomasters.core.pref.PreferenceHelper
import com.mas.cryptomasters.data.response.home.Coin
import com.mas.cryptomasters.data.response.notifications.NotificationsData
import com.mas.cryptomasters.databinding.CoinsItemsBinding
import com.mas.cryptomasters.databinding.NotificationsItemsBinding
import com.mas.cryptomasters.ui.othersActivity.NavigationActivity
import com.mas.cryptomasters.utils.Extensions.loadWebImage
import com.mas.cryptomasters.utils.Extensions.navigateToActivity
import com.mas.cryptomasters.utils.Extensions.setTextWithTowColor
import com.mas.cryptomasters.utils.Navigate
import java.util.*
import kotlin.collections.ArrayList

class NotificationsAdapter(val preferenceHelper: PreferenceHelper, val activity: Activity) :
    RecyclerView.Adapter<NotificationsAdapter.MyViewHolder>() {
    private val postActions = listOf("like", "comment")
    private var data: ArrayList<NotificationsData> = ArrayList()
    private lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding: NotificationsItemsBinding =
            NotificationsItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        context = parent.context
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        data[position]
            .let {
                holder.setView(it)
            }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateAdapter(newData: List<NotificationsData>) {
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged()
    }

    fun getNotification(position: Int): NotificationsData {
        return data[position]
    }

    @SuppressLint("NotifyDataSetChanged")
    fun removeNotification(item: NotificationsData) {
        data.remove(item)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = data.size

    inner class MyViewHolder(private val binding: NotificationsItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setView(it: NotificationsData) {
            binding.tvType.text = it.action
            if (it.user != null) {
                binding.ivLogoApp.visibility = View.GONE
                context.setTextWithTowColor(
                    binding.tvText,
                    string1 = it.title,
                    string2 = it.body,
                    color1 = "#FDBA21",
                    color2 = "#00235E"
                )
            }

            if ((it.action + "").lowercase() == "admin") {
                binding.ivLogoApp.visibility = View.VISIBLE
                binding.ivLogo.visibility = View.GONE
            } else {
                binding.ivLogo.loadWebImage(it.user!!.avatar + "")
                binding.ivLogoApp.visibility = View.GONE
                binding.ivLogo.visibility = View.VISIBLE
            }

            binding.clItem.setOnClickListener { _ ->
                if (it.action != "admin" && it.postId != "0") {
                    val bundle = Bundle()
                    if (it.postId != "0" && postActions.indexOf(it.action) != -1) {
                        bundle.putString(Navigate.TARGET, Navigate.POST_DETAILS)
                        bundle.putString(Navigate.DATA_ID, it.postId)
                    } else if (it.action == "recommend") {
                        bundle.putString(Navigate.TARGET, Navigate.RECOMMEND_FRAGMENT)
                    }
                    activity.navigateToActivity(NavigationActivity::class.java, bundle = bundle)
                }
            }

        }
    }


}



