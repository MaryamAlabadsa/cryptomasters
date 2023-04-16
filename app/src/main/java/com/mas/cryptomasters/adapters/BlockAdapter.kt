package com.mas.cryptomasters.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.mas.cryptomasters.data.response.block.BlockData
import com.mas.cryptomasters.databinding.UserListBinding
import com.mas.cryptomasters.utils.Extensions.loadWebImage

class BlockAdapter : RecyclerView.Adapter<BlockAdapter.MyViewHolder>() {
    var data = ArrayList<BlockData>()
    val mutableUser = MutableLiveData<BlockData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = UserListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        data[position]
            .let {
                holder.bindView(data[position])
            }
    }

    override fun getItemCount(): Int = data.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateAdapter(newData: List<BlockData>) {
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun deleteItem(blockData: BlockData) {
        if (data.contains(blockData)) {
            data.remove(blockData)
            notifyDataSetChanged()
        }

    }

    inner class MyViewHolder(val binding: UserListBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bindView(it: BlockData) {
            binding.ivImage.loadWebImage(it.blockObject.avatar, true)
            binding.tvName.text = it.blockObject.name

            binding.unBlock.setOnClickListener { _ ->
                mutableUser.postValue(it)
            }
        }

    }

}