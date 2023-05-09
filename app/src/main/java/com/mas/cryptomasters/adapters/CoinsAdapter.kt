package com.mas.cryptomasters.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.mas.cryptomasters.R
import com.mas.cryptomasters.data.response.home.Coin
import com.mas.cryptomasters.data.response.recommendations.RecommendData
import com.mas.cryptomasters.databinding.CoinsItemsBinding
import com.mas.cryptomasters.utils.Extensions.loadWebImage

class CoinsAdapter : RecyclerView.Adapter<CoinsAdapter.MyViewHolder>() {
    private var data: ArrayList<Coin> = ArrayList()
    private lateinit var context: Context
    var mutableSelectedCoin = MutableLiveData<Coin>()
    var currentCoin: Coin? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding: CoinsItemsBinding =
            CoinsItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
    fun updateAdapter(newData: List<Coin>) {
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = data.size

    inner class MyViewHolder(private val binding: CoinsItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setView(it: Coin) {
            binding.tvName.text = it.name
            binding.tvStatus.text = it.halal

            binding.clCard.setOnClickListener { _ ->
                currentCoin = it
                mutableSelectedCoin.postValue(it)
            }


            if (it.isHalal == "0") {
                binding.tvStatus.setTextColor(context.getColor(R.color.green))
            } else {
                binding.tvStatus.setTextColor(context.getColor(R.color.red))
            }

            if (it.image != null) {
                binding.clImage.visibility = View.VISIBLE
                binding.ivImage.loadWebImage(it.image + "", isProfile = false)
            } else {
                binding.clImage.visibility = View.GONE
            }

            if (it.icon != null) {
                binding.cvLogo.loadWebImage(it.icon + "", isProfile = false)
            }


        }

    }
    fun clearAdapter(){
        this.data.clear()
        notifyDataSetChanged()
    }
    fun updateAdapter(coinList: ArrayList<Coin>, searchQuery: String? = null) {
        this.data = coinList.toMutableList() as ArrayList<Coin>

        if (searchQuery != null) {
            val filteredList = coinList.filter { it.name.contains(searchQuery, ignoreCase = true) }
            this.data = filteredList.toMutableList() as ArrayList<Coin>
        }

        notifyDataSetChanged()
    }

}



