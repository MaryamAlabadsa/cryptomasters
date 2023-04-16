package com.mas.cryptomasters.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mas.cryptomasters.core.pref.PreferenceHelper
import com.mas.cryptomasters.data.models.Plan
import com.mas.cryptomasters.databinding.PlanItemBinding

class PlanAdapter(val preferenceHelper: PreferenceHelper, val activity: Activity) :
    RecyclerView.Adapter<PlanAdapter.MyViewHolder>() {
    //    private val postActions = listOf("like", "comment")
    private var data: ArrayList<Plan> = ArrayList()
    private lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlanAdapter.MyViewHolder {
        val binding: PlanItemBinding =
            PlanItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
    fun updateAdapter(newData: List<Plan>) {
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = data.size

    inner class MyViewHolder(private val binding: PlanItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setView(it: Plan) {
            binding.name.text = it.name
            binding.body.text = it.body.toString()
            binding.adsPrice.text = it.ads_price.toString()+" $"
        }

    }
}
