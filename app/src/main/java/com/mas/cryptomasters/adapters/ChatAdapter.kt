package com.mas.cryptomasters.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mas.cryptomasters.core.pref.PreferenceHelper
import com.mas.cryptomasters.data.models.ChatModels
import com.mas.cryptomasters.databinding.ChatItemsBinding
import com.mas.cryptomasters.utils.Extensions.loadWebImage
import com.mas.cryptomasters.utils.Extensions.setLongTimeFormat

class ChatAdapter(val preferenceHelper: PreferenceHelper) :
    RecyclerView.Adapter<ChatAdapter.MyViewHolder>() {
    var data = ArrayList<ChatModels>()
    private lateinit var context: Context


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ChatItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        context = parent.context
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        data[position].let {
            holder.setView(it)
        }
    }

    override fun getItemCount(): Int = data.size

    fun updateChats(newData: ArrayList<ChatModels>) {
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged()
    }

    inner class MyViewHolder(val binding: ChatItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setView(it: ChatModels) {
            if (it.idUser == preferenceHelper.getUserProfile().id) {
                binding.SenderMessageLayout.visibility = View.VISIBLE
                binding.receivedMessageLayout.visibility = View.GONE

                binding.tvSenderMessage.text = it.message
                binding.ivSenderImage.loadWebImage(it.avatar + "")
                if (it.timestamp != null) {
                    binding.tvSendTime.text = it.timestamp.time.setLongTimeFormat(context)
                }else{
                    binding.tvSendTime.text = System.currentTimeMillis().setLongTimeFormat(context)
                }
            } else {
                binding.SenderMessageLayout.visibility = View.GONE
                binding.receivedMessageLayout.visibility = View.VISIBLE

                binding.tvReceivedMessage.text = it.message
                binding.ivReceivedImage.loadWebImage(it.avatar + "")

                if (it.timestamp != null) {
                    binding.tvReceivedTime.text = it.timestamp.time.setLongTimeFormat(context)
                } else {
                    binding.tvReceivedTime.text = System.currentTimeMillis().setLongTimeFormat(context)

                }
            }

        }
    }
}