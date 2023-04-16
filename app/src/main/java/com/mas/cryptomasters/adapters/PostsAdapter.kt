package com.mas.cryptomasters.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.mas.cryptomasters.R
import com.mas.cryptomasters.core.pref.PreferenceHelper
import com.mas.cryptomasters.data.request.LikePostRequest
import com.mas.cryptomasters.data.response.home.Like
import com.mas.cryptomasters.data.response.home.PostsData
import com.mas.cryptomasters.databinding.PostsItemsBinding
import com.mas.cryptomasters.ui.dialogs.EditPostDialog
import com.mas.cryptomasters.ui.dialogs.UsersDialog
import com.mas.cryptomasters.ui.fragment.posts.PostsViewModel
import com.mas.cryptomasters.ui.othersActivity.NavigationActivity
import com.mas.cryptomasters.ui.othersActivity.PlayerActivity
import com.mas.cryptomasters.utils.Extensions
import com.mas.cryptomasters.utils.Extensions.askLogin
import com.mas.cryptomasters.utils.Extensions.crToast
import com.mas.cryptomasters.utils.Extensions.loadWebImage
import com.mas.cryptomasters.utils.Extensions.navigateToActivity
import com.mas.cryptomasters.utils.Extensions.reLogin
import com.mas.cryptomasters.utils.Extensions.setTimeFormat
import com.mas.cryptomasters.utils.Extensions.showProgress
import com.mas.cryptomasters.utils.Navigate
import com.mas.cryptomasters.utils.PostsActions
import com.mas.cryptomasters.utils.ToastType


class PostsAdapter(
    private val activity: Activity,
    private val viewModel: PostsViewModel,
    private val owner: LifecycleOwner,
    private var manger: FragmentManager
) : RecyclerView.Adapter<PostsAdapter.MyViewHolder>() {
    private val data = ArrayList<PostsData>()
    private lateinit var preferenceHelper: PreferenceHelper
    private var usersDialog: UsersDialog? = null
    private var thePosition: Int? = null
    private lateinit var context: Context
    private var editPostDialog: EditPostDialog? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding: PostsItemsBinding =
            PostsItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        preferenceHelper = PreferenceHelper(parent.context)
        usersDialog = UsersDialog(activity, viewModel, preferenceHelper)
        context = parent.context
        editPostDialog = EditPostDialog(context)
        return MyViewHolder(binding)
    }


    init {
        // observe delete
        viewModel.postFromDetailsMutable.observe(owner) {
            Extensions.hideProgress()
            when {
                it.reLogin -> activity.reLogin(preferenceHelper)
                it.error.isNotEmpty() -> activity.crToast()
                it.data != null -> {
                    when (it.flag) {
                        0 -> whenLikeOrCommentSet(it.data.toString())
                        1 -> updateSinglePost(it.data as PostsData)
                        -1 -> whenPostDelete(it.data as PostsData, false)
                        -2 -> whenPostDelete(it.data as PostsData, true)
                    }
                    it.data = null
                }
            }
        }

    }

    private fun whenPostDelete(postsData: PostsData, isBlocked: Boolean = false) {
        if (isBlocked)
            activity.crToast(context.getString(R.string.user_was_block), ToastType.SUCCESS)
        else
            activity.crToast(context.getString(R.string.post_delete_success), ToastType.SUCCESS)

        deletePost(postsData)
    }

    private fun whenLikeOrCommentSet(postId: String) {
        viewModel.postById(postId)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        data[position].let {
            holder.setView(it, position)
        }
    }

    override fun getItemCount(): Int = data.size
    inner class MyViewHolder(val binding: PostsItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("NotifyDataSetChanged")
        fun setView(it: PostsData, position: Int) {
            // post data
            binding.ivProfile.loadWebImage(it.user!!.avatar + "")
            binding.tvName.text = it.user.name
            binding.tvDate.text = it.createdAt!!.setTimeFormat(binding.tvDate.context)
            binding.tvTitle.text = it.title
            binding.tvDescription.text = it.content
            binding.tvLikeNum.text = it.likesCount
            binding.tvCommentNum.text = it.commentsCount


            //set images
            if (it.galleries != null && it.galleries.isNotEmpty()) {
                binding.clImage.visibility = View.VISIBLE
                binding.clVideo.visibility = View.GONE
                binding.line.visibility = View.GONE

                binding.ivPostImage1.loadWebImage(it.galleries[0].image, false)

                binding.linerMoreThan1Image.visibility = View.GONE
                binding.ivPostImage2.visibility = View.GONE
                binding.tvPostImageMore2.visibility = View.GONE

                if (it.galleries.size >= 2) {
                    binding.line.visibility = View.VISIBLE
                    binding.linerMoreThan1Image.visibility = View.VISIBLE
                    binding.ivPostImage2.loadWebImage(it.galleries[1].image, false)
                    binding.ivPostImage2.visibility = View.VISIBLE

                    if (it.galleries.size > 2) {
                        binding.tvPostImageMore2.visibility = View.VISIBLE
                        binding.tvPostImageMore2.text = "+${((it.galleries.size) - 2)}"
                    }
                }
            } else if (it.video != null && it.video.isNotEmpty() && it.video != "null") {
                binding.clImage.visibility = View.GONE
                binding.clVideo.visibility = View.VISIBLE
                binding.line.visibility = View.GONE
                binding.vVideo.visibility = View.VISIBLE
                binding.ivPlayLogo.visibility = View.VISIBLE
                binding.vVideo.loadWebImage(it.video, isProfile = false)
            } else {
                binding.clImage.visibility = View.GONE
                binding.clVideo.visibility = View.GONE
            }

            // show user comment
            binding.tvCommentNum.setOnClickListener { _ ->
                showCommentDialog(it)
            }

            // show user likes
            binding.tvLikeNum.setOnClickListener { _ ->
                showLikeDialog(it)
            }

            if (it.isLike == 1) {
                binding.ivSetLike.setImageResource(R.drawable.ic_like_fill)
            } else {
                binding.ivSetLike.setImageResource(R.drawable.ic_heart)
            }


            binding.ivSetLike.setOnClickListener { _ ->
                if (!preferenceHelper.isGustUser()) {
                    if (it.isLike != 1) {
                        data[position].isLike = 1
                        data[position].likesCount =
                            (data[position].likesCount!!.toInt() + 1).toString()
                        notifyDataSetChanged()

                        thePosition = position
                        viewModel.likePostRequest = LikePostRequest(postId = it.id!!)
                        viewModel.setLike()

                    } else {
                        data[position].isLike = 0
                        data[position].likesCount =
                            (data[position].likesCount!!.toInt() - 1).toString()
                        notifyDataSetChanged()

                        thePosition = position
                        viewModel.likePostRequest = LikePostRequest(postId = it.id!!)

                        viewModel.removeLike()
                    }
                } else {
                    activity.askLogin()
                }

            }

            // edit or delete
            binding.ivEdit.setOnClickListener { _ ->
                editPostDialog
                    ?.setPostAction(object : PostsActions {
                        @SuppressLint("NotifyDataSetChanged")
                        override fun onDelete() {
                            context.showProgress()
                            viewModel.deletePost(it)
                        }

                        override fun onEdit() {
                            val bundle = Bundle()
                            bundle.putString(Navigate.TARGET, Navigate.EDIT_POST)
                            bundle.putString(Navigate.SOURCE, activity.localClassName)
                            bundle.putParcelable(Navigate.DATA_POST, it)
                            activity.navigateToActivity(
                                NavigationActivity::class.java,
                                bundle = bundle
                            )
                        }

                        override fun onReport() {
                            val bundle = Bundle()
                            bundle.putString(Navigate.TARGET, Navigate.REPORT)
                            activity.navigateToActivity(
                                NavigationActivity::class.java,
                                bundle = bundle
                            )
                        }

                        override fun onBlock() {
                            context.showProgress()
                            viewModel.blockUser(it)
                        }
                    })
                    ?.setSettings(
                        preferenceHelper.getUserProfile().id == it.user.id,
                        isGuest = preferenceHelper.isGustUser()
                    )
                    ?.show()
            }

            binding.clPost.setOnClickListener { _ ->
                goToDetails(it)
            }

            // open the images slide
            binding.clImage.setOnClickListener { _ ->
                activity.startActivity(
                    Intent(context, PlayerActivity::class.java)
                        .putExtra("postData", it)
                )

            }

            //show video
            binding.vVideo.setOnClickListener { _ ->
                activity.startActivity(
                    Intent(context, PlayerActivity::class.java)
                        .putExtra("videoUrl", it.video)
                )
            }
        }


    }

    fun showCommentDialog(data: PostsData, isDetails: Boolean = false) {
        usersDialog!!.setCommentList(data, isDetails)
        usersDialog!!.show(manger, "comment")
    }

    fun showLikeDialog(data: PostsData) {
        usersDialog!!.setLikeList(data)
        usersDialog!!.show(manger, "comment")
    }


    @SuppressLint("NotifyDataSetChanged")
    fun updateAdapter(newData: List<PostsData>) {
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateSinglePost(newPostsData: PostsData) {
        if (usersDialog != null)
            if (usersDialog!!.isAdded) {
                usersDialog!!.dismiss()
            }
        for (post in data) {
            if (post.id == newPostsData.id) {
                data[data.indexOf(post)] = newPostsData
                notifyDataSetChanged()
                return
            }
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateNewLike(position: Int, newLike: Like) {
        data[position].likes!!.add(newLike)
        notifyItemChanged(position)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun deletePost(post: PostsData) {
        val itr = data.iterator()
        while (itr.hasNext()) {
            val thisPost = itr.next()
            if (thisPost.id == post.id) {
                itr.remove()
                notifyDataSetChanged()
            }
        }
    }


    @SuppressLint("NotifyDataSetChanged")
    fun updateNewLikeWithPost(currentData: PostsData, newLike: Like) {
        for (post in data) {
            if (post.id == currentData.id) {
                data[data.indexOf(post)].likesCount =
                    (data[data.indexOf(post)].likesCount!!.toInt() + 1).toString()
                data[data.indexOf(post)].likes!!.add(newLike)
                data[data.indexOf(post)].isLike = 1
                notifyDataSetChanged()
                return
            }
        }
    }

    private fun goToDetails(postsData: PostsData) {
        val bundle = Bundle()
        bundle.putString(Navigate.TARGET, Navigate.POST_DETAILS)
        bundle.putParcelable(Navigate.DATA_POST, postsData)
        activity.navigateToActivity(NavigationActivity::class.java, bundle = bundle)
    }
}


