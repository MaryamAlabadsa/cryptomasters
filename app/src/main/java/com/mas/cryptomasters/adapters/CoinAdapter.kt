package com.mas.cryptomasters.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mas.cryptomasters.R
import com.mas.cryptomasters.core.pref.PreferenceHelper
import com.mas.cryptomasters.data.models.ChartResponse
import com.mas.cryptomasters.data.models.coin.Coins
import com.mas.cryptomasters.databinding.CoinBinding
import com.mas.cryptomasters.ui.chart.ChartActivity
import com.mas.cryptomasters.utils.Extensions.loadWebImage
import java.util.logging.Logger
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
            var name = it.name
            var high_24h = it.high_24h
            var low_24h = it.low_24h
            binding.name.text = name
            binding.coinPrice.text = it.current_price.toString()

            val rounded1 = it.price_change_percentage_24h?.roundToLong()
            val output = (rounded1?.toDouble() ?: 0.0) / 100.0

            binding.currency.text = output.toString()
//            binding.image.loadWebImage(it.image, false)
            Glide.with(context)
                .load(it.image)
//                .override(this.width, this.height)
                .placeholder( R.drawable.ic_image_fream)
                .into(binding.image)
//            Toast.makeText(context, data.size.toString(), Toast.LENGTH_LONG).show()
            val Log = Logger.getLogger(ChartResponse::class.java.name)
            Log.warning("${data.size} kkkkkkkkkkkkkkkkkk")
            binding.root.setOnClickListener {
                val intent = Intent(context, ChartActivity::class.java)
                intent.putExtra(name, "marketId")
                preferenceHelper.setMarketId(name,high_24h,low_24h)

                context.startActivity(intent)
            }

        }

    }
}
