package com.mas.cryptomasters.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mas.cryptomasters.R
import com.mas.cryptomasters.core.pref.PreferenceHelper
import com.mas.cryptomasters.data.response.EditCommentRequest
import com.mas.cryptomasters.data.response.home.Comment
import com.mas.cryptomasters.databinding.UserItemsBinding
import com.mas.cryptomasters.ui.dialogs.EditPostDialog
import com.mas.cryptomasters.ui.fragment.posts.PostsViewModel
import com.mas.cryptomasters.ui.othersActivity.NavigationActivity
import com.mas.cryptomasters.utils.Extensions.loadWebImage
import com.mas.cryptomasters.utils.Extensions.navigateToActivity
import com.mas.cryptomasters.utils.Extensions.setTimeFormat
import com.mas.cryptomasters.utils.Extensions.showProgress
import com.mas.cryptomasters.utils.Navigate
import com.mas.cryptomasters.utils.PostsActions

class CommentAdapter(
    val viewModel: PostsViewModel, val activity: Activity
) :
    RecyclerView.Adapter<CommentAdapter.MyViewHolder>() {
    private var data: ArrayList<Comment> = ArrayList()
    private lateinit var preferenceHelper: PreferenceHelper
    private lateinit var editCommentDialog: EditPostDialog

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding: UserItemsBinding =
            UserItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        preferenceHelper = PreferenceHelper(parent.context)
        editCommentDialog = EditPostDialog(parent.context)

        return MyViewHolder(binding)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        data[position]
            .let {
                holder.setView(it)
            }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateAdapter(newData: List<Comment>) {
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = data.size

    inner class MyViewHolder(private val binding: UserItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setView(it: Comment) {
            binding.ivProfile.loadWebImage(it.user.avatar + "")
            binding.tvName.text = it.user.name
            binding.tvDate.text = it.createdAt.setTimeFormat(binding.tvDate.context)
            binding.tvComment.setText(it.comment)
            binding.tvComment.visibility = View.VISIBLE



            binding.tvEdit.setOnClickListener { _ ->
                if (binding.tvComment.text.toString().trim().isNotEmpty()) {
                    viewModel.editComment = EditCommentRequest(
                        comment = binding.tvComment.text.toString()
                    )
                    binding.tvEdit.visibility = View.GONE
                    binding.tvComment.isEnabled = false
                    binding.tvComment.setBackgroundColor(activity.getColor(R.color.white))

                    activity.showProgress()
                    viewModel.editComment(it)
                } else {
                    binding.tvComment.error = "*"
                }
            }
            // delete
            binding.ivEdit.setOnClickListener { _ ->
                editCommentDialog.setPostAction(object : PostsActions {
                    @SuppressLint("NotifyDataSetChanged")
                    override fun onDelete() {
                        viewModel.deleteComment(it)
                        activity.showProgress()
                    }

                    override fun onEdit() {
                        binding.tvEdit.visibility = View.VISIBLE
                        binding.tvComment.isEnabled = true
                        binding.tvComment.setBackgroundColor(activity.getColor(R.color.light_gray))
                    }

                    override fun onReport() {
                        val bundle = Bundle()
                        bundle.putString(Navigate.TARGET, Navigate.REPORT)
                        activity.navigateToActivity(NavigationActivity::class.java, bundle = bundle)
                    }

                    override fun onBlock() {
                    }
                }).setSettings(
                    preferenceHelper.getUserProfile().id == it.user.id,
                    isFromComment = true,
                    isGuest = preferenceHelper.isGustUser()
                )
                    .show()
            }
        }


    }

}



