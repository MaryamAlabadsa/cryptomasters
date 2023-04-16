package com.mas.cryptomasters.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.mas.cryptomasters.core.pref.PreferenceHelper
import com.mas.cryptomasters.data.models.coin.Coins
import com.mas.cryptomasters.data.response.CoinsResponse
import com.mas.cryptomasters.databinding.CoinBinding
import com.mas.cryptomasters.utils.Extensions.loadWebImage
import kotlin.math.roundToInt
import kotlin.math.roundToLong

class CoinAdapter(val preferenceHelper: PreferenceHelper, val activity: Activity) :
    RecyclerView.Adapter<CoinAdapter.MyViewHolder>() {
    //    private val postActions = listOf("like", "comment")
    private var data: ArrayList<Coins> = ArrayList()
    private lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinAdapter.MyViewHolder {
        val binding: CoinBinding =
            CoinBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
    fun updateAdapter(newData: List<Coins>) {
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = data.size

    inner class MyViewHolder(private val binding: CoinBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setView(it: Coins) {
            binding.name.text = it.name
            binding.coinPrice.text = it.current_price.toString()

            val rounded1 = it.price_change_percentage_24h?.roundToLong()
            val output = (rounded1?.toDouble() ?:0.0 ) / 100.0

            binding.currency.text = output.toString()
            binding.image.loadWebImage(it.image, false)

        }

    }
}
