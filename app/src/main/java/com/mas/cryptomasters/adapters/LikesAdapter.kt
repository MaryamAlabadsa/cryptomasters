package com.mas.cryptomasters.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mas.cryptomasters.data.response.home.Like
import com.mas.cryptomasters.databinding.UserItemsBinding
import com.mas.cryptomasters.utils.Extensions.loadWebImage
import com.mas.cryptomasters.utils.Extensions.setTimeFormat

class LikesAdapter :
    RecyclerView.Adapter<LikesAdapter.MyViewHolder>() {
    private var data: ArrayList<Like> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding: UserItemsBinding = UserItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        data[position]
            .let {
                holder.setView(it)
            }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateAdapter(newData: List<Like>) {
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = data.size

    inner class MyViewHolder(private val binding: UserItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setView(it: Like) {
            binding.tvComment.visibility = View.GONE
            binding.ivProfile.loadWebImage(it.user.avatar+"")
            binding.tvName.text = it.user.name
            binding.tvDate.text = it.createdAt.setTimeFormat(binding.tvDate.context)
        }

    }
}



