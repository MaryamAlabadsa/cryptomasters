package com.mas.cryptomasters.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.mas.cryptomasters.R
import com.mas.cryptomasters.core.pref.PreferenceHelper
import com.mas.cryptomasters.data.response.recommendations.RecommendData
import com.mas.cryptomasters.databinding.RecommendItemsBinding
import com.mas.cryptomasters.ui.othersActivity.NavigationActivity
import com.mas.cryptomasters.utils.Constants
import com.mas.cryptomasters.utils.Extensions.loadWebImage
import com.mas.cryptomasters.utils.Extensions.percentageFormat
import com.mas.cryptomasters.utils.Extensions.setTimeFormat
import com.mas.cryptomasters.utils.Navigate

class RecommendAdapter : RecyclerView.Adapter<RecommendAdapter.MyViewHolder>() {
    private var data: ArrayList<RecommendData> = ArrayList()
    private lateinit var context: Context
    private lateinit var preferenceHelper: PreferenceHelper

    var showAdsObservable = MutableLiveData<Boolean>()
    var currentData: RecommendData? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding: RecommendItemsBinding =
            RecommendItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        context = parent.context
        preferenceHelper = PreferenceHelper(context)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        data[position].let { holder.setView(it) }

        holder.binding.llContent.setOnClickListener {
            currentData = data[position]
            showAdsObservable.postValue(true)
//            if (preferenceHelper.getAdsCount() >= Constants.ADS_COUNT) {
//                preferenceHelper.incrementAdsCount(true).let {
//                    showAdsObservable.postValue(true)
//                }
//            } else {
//                preferenceHelper.incrementAdsCount(false).let {
//                    showAdsObservable.postValue(false)
//                }
//
//            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateAdapter(newData: List<RecommendData>) {
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = data.size

    inner class MyViewHolder(val binding: RecommendItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setView(it: RecommendData) {
            binding.tvTitle.text = it.title
            binding.tvDate.text = it.createdAt.setTimeFormat(binding.tvDate.context)
            binding.tvText.text = it.content
            binding.ivPostImage.loadWebImage(it.image + "", isProfile = false)

            binding.pbUserRate.progress = it.upCount.toInt()
            "${it.upCount.toString().percentageFormat()} ${context.getString(R.string.up)}".also { binding.tvUpValue.text = it }
            "${
                it.downCount.toString().percentageFormat()
            } ${context.getString(R.string.down)}".also { binding.tvDownValue.text = it }
        }

    }
}



