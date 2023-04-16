package com.mas.cryptomasters.ui.dialogs

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mas.cryptomasters.R
import com.mas.cryptomasters.adapters.CommentAdapter
import com.mas.cryptomasters.adapters.LikesAdapter
import com.mas.cryptomasters.core.pref.PreferenceHelper
import com.mas.cryptomasters.data.response.home.PostsData
import com.mas.cryptomasters.databinding.UsersDialogBinding
import com.mas.cryptomasters.ui.fragment.posts.PostsViewModel
import com.mas.cryptomasters.ui.othersActivity.NavigationActivity
import com.mas.cryptomasters.utils.Navigate

class UsersDialog(
    private val activity: Activity,
    private val viewModel: PostsViewModel,
    private val preferenceHelper: PreferenceHelper
) :
    BottomSheetDialogFragment() {

    private lateinit var binding: UsersDialogBinding
    private var commentAdapter: CommentAdapter? = null
    private var likesAdapter: LikesAdapter = LikesAdapter()
    private var isLikeList = false
    private var isDetails = false
    private lateinit var data: PostsData

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = UsersDialogBinding.inflate(layoutInflater)
        binding.rvUsers.layoutManager = LinearLayoutManager(context)

        commentAdapter = CommentAdapter(viewModel, activity)


        if (isLikeList) {
            likesAdapter.updateAdapter(data.likes!!)

            binding.clComment.visibility = View.GONE
            binding.rvUsers.adapter = likesAdapter
        } else {
            commentAdapter?.updateAdapter(data.comments!!)
            if (isDetails) {
                binding.tvWriteComment.visibility = View.GONE
            } else {
                binding.tvWriteComment.visibility = View.VISIBLE
            }


            if (preferenceHelper.isGustUser()) {
                binding.tvWriteComment.visibility = View.GONE
            }else{
                binding.clComment.visibility = View.VISIBLE
            }

            "${commentAdapter!!.itemCount} ${getString(R.string.comment)}".also {
                binding.tvCommentNum.text = it
            }
            binding.rvUsers.adapter = commentAdapter
        }

        // write comment
        binding.tvWriteComment.setOnClickListener {
            dismiss()
            context?.startActivity(
                Intent(context, NavigationActivity::class.java)
                    .putExtra(Navigate.TARGET, Navigate.POST_DETAILS)
                    .putExtra(Navigate.DATA_POST, data)
            )
        }

        return binding.root
    }

    fun setCommentList(data: PostsData, fromDetails: Boolean = false): UsersDialog {
        this.data = data
        isLikeList = false
        isDetails = fromDetails
        return this
    }

    fun setLikeList(data: PostsData): UsersDialog {
        this.data = data
        isLikeList = true
        return this
    }

}