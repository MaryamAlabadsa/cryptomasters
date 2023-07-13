package com.mas.cryptomasters.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.mas.cryptomasters.R
import com.mas.cryptomasters.core.pref.PreferenceHelper
import com.mas.cryptomasters.data.response.Analytic
import com.mas.cryptomasters.data.response.AnalyticData
import com.mas.cryptomasters.data.response.recommendations.RecommendData
import com.mas.cryptomasters.databinding.AnalyticsItemBinding
import com.mas.cryptomasters.databinding.RecommendItemsBinding
import com.mas.cryptomasters.utils.Extensions.percentageFormat
import com.mas.cryptomasters.utils.Extensions.setTimeFormat

class AnalyticAdapter : RecyclerView.Adapter<AnalyticAdapter.MyViewHolder>() {
    private var data: ArrayList<AnalyticData> = ArrayList()
    private lateinit var context: Context
    private lateinit var preferenceHelper: PreferenceHelper

    //    var currentData: AnalyticData? = null
    val currentData: MutableLiveData<AnalyticData> = MutableLiveData()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding: AnalyticsItemBinding =
            AnalyticsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        context = parent.context
        preferenceHelper = PreferenceHelper(context)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        data[position].let { holder.setView(it) }

        holder.binding.root.setOnClickListener {
            currentData.value = data[position]
        }

    }


    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: List<AnalyticData>) {
        data.clear()
        data = list as ArrayList<AnalyticData>
        notifyDataSetChanged()
    }
//
//    fun addToList(myList: List<RecommendData>) {
//        data.addAll(myList)
//        notifyDataSetChanged()
//    }

    override fun getItemCount(): Int = data.size

    inner class MyViewHolder(val binding: AnalyticsItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setView(it: AnalyticData) {
            binding.tvTitle.text = it.title
            binding.tvDate.text = it.createdAt?.setTimeFormat(binding.tvDate.context) ?: ""
        }

    }
}



