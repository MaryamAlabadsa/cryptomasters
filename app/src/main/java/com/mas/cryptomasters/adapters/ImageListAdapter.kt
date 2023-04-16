package com.mas.cryptomasters.adapters

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import android.view.Display
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.mas.cryptomasters.data.response.home.Galleries
import com.mas.cryptomasters.databinding.ImagesItemsBinding
import com.mas.cryptomasters.utils.Extensions.loadWebImage

class ImageListAdapter : RecyclerView.Adapter<ImageListAdapter.MyViewHolder>() {
    var data = ArrayList<String>()
    var removedImages = ArrayList<Int>()
    var currentImageList = ArrayList<String>()
    var mutableEmptyArray = MutableLiveData<Int>()

    var idsGalleriesMap = HashMap<String, Int>()
    var isDisplay = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ImagesItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        data[position].let {
            holder.bindView(it, position)
        }
    }

    override fun getItemCount(): Int = data.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(list: ArrayList<String>, isFromEdit: Boolean = false) {
        if (!isFromEdit) {
            data.clear()
        }
        data.addAll(list)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateListToEdit(galleries: List<Galleries>, isDisplay: Boolean = false) {
        data.clear()
        removedImages.clear()
        currentImageList.clear()
        idsGalleriesMap.clear()

        for (image in galleries) {
            idsGalleriesMap[image.image] = image.id
            currentImageList.add(image.image)
        }
        data.addAll(currentImageList)
        this.isDisplay = isDisplay
        notifyDataSetChanged()
    }

    inner class MyViewHolder(val binding: ImagesItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
        fun bindView(it: String, position: Int) {
            binding.tvNum.text = "${position + 1}/${data.size}"


            if (isDisplay) {
                binding.ivSliderPhoto.loadWebImage(
                    it + "",
                    isProfile = false,
                    res = !currentImageList.contains(it)
                )
                binding.clEditableImages.visibility = View.GONE
            } else {
                binding.ivPostImage.loadWebImage(
                    it + "",
                    isProfile = false,
                    res = !currentImageList.contains(it)
                )
                binding.clEditableImages.visibility = View.VISIBLE
            }

            binding.ivDeleteFile.setOnClickListener { _ ->
                if (idsGalleriesMap[it] != null) {
                    removedImages.add(idsGalleriesMap[it]!!)
                }
                data.removeAt(position)
                mutableEmptyArray.postValue(data.size)
                notifyDataSetChanged()
            }
        }
    }

}