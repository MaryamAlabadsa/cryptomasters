package com.mas.cryptomasters.ui.fragment.posts

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.mas.cryptomasters.BaseFragment
import com.mas.cryptomasters.R
import com.mas.cryptomasters.data.request.CommentRequest
import com.mas.cryptomasters.data.request.LikePostRequest
import com.mas.cryptomasters.data.response.home.PostsData
import com.mas.cryptomasters.databinding.FragmentPostDetailsBinding
import com.mas.cryptomasters.ui.dialogs.EditPostDialog
import com.mas.cryptomasters.ui.dialogs.UsersDialog
import com.mas.cryptomasters.ui.main.MainActivity
import com.mas.cryptomasters.ui.othersActivity.NavigationActivity
import com.mas.cryptomasters.ui.othersActivity.PlayerActivity
import com.mas.cryptomasters.utils.Extensions.askLogin
import com.mas.cryptomasters.utils.Extensions.crToast
import com.mas.cryptomasters.utils.Extensions.hideProgress
import com.mas.cryptomasters.utils.Extensions.loadWebImage
import com.mas.cryptomasters.utils.Extensions.navigateToActivity
import com.mas.cryptomasters.utils.Extensions.reLogin
import com.mas.cryptomasters.utils.Extensions.setTimeFormat
import com.mas.cryptomasters.utils.Extensions.showProgress
import com.mas.cryptomasters.utils.Navigate
import com.mas.cryptomasters.utils.PostsActions
import com.mas.cryptomasters.utils.ToastType
import com.mas.cryptomasters.utils.UpdateData
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostDetailsFragment :
    BaseFragment<FragmentPostDetailsBinding>() {


    override fun getViewBinding() = FragmentPostDetailsBinding.inflate(layoutInflater)

    private lateinit var editPostDialog: EditPostDialog
    private lateinit var currentPostsData: PostsData
    private var usersDialog: UsersDialog? = null

    private val viewModel: PostsViewModel by viewModels()


    override fun init() {

        if (!this.isAdded) {
            return
        }

        if (preferences.isGustUser()) {
            binding.clCommentArea.visibility = View.GONE
        }

        editPostDialog = EditPostDialog(requireContext())
        usersDialog = UsersDialog(requireActivity(), viewModel, preferences)

        if (arguments?.containsKey(Navigate.DATA_POST) == true) {
            if (requireArguments().getParcelable<PostsData>(Navigate.DATA_POST) != null) {
                currentPostsData = requireArguments().getParcelable(Navigate.DATA_POST)!!
                loadPostData(currentPostsData)
            }
        } else if (arguments?.containsKey(Navigate.DATA_ID) == true) {
            if (requireArguments().getString(Navigate.DATA_ID) != null) {
                viewModel.postById(requireArguments().getString(Navigate.DATA_ID)!!)
            }
        }

        // edit or delete
        binding.ivEdit.setOnClickListener {
            editPostDialog.setPostAction(object : PostsActions {
                override fun onDelete() {
                    requireActivity().showProgress()
                    viewModel.deletePost(currentPostsData)
                }

                override fun onEdit() {
                    val bundle = Bundle()
                    bundle.putString(Navigate.TARGET, Navigate.EDIT_POST)
                    bundle.putParcelable(Navigate.DATA_POST, currentPostsData)
                    requireActivity().navigateToActivity(
                        NavigationActivity::class.java,
                        bundle = bundle
                    )
                }

                override fun onReport() {
                    val bundle = Bundle()
                    bundle.putString(Navigate.TARGET, Navigate.REPORT)
                    requireActivity().navigateToActivity(
                        NavigationActivity::class.java,
                        bundle = bundle
                    )
                }

                override fun onBlock() {
                    requireContext().showProgress()
                    viewModel.blockUser(currentPostsData)
                }
            }).setSettings(
                currentPostsData.user!!.id == preferences.getUserProfile().id,
                isGuest = preferences.isGustUser()
            )
                .show()
        }

        // observe delete
        viewModel.postFromDetailsMutable.observe(this) {
            hideProgress()

            binding.txtComment.isEnabled = true
            binding.tvSend.isClickable = true
            binding.pbCommentLoad.visibility = View.GONE

            when {
                it.reLogin -> requireActivity().reLogin(preferences)
                it.error.isNotEmpty() -> {
                    handleLoading(binding.loading, true)
                    requireContext().crToast()
                }
                it.data != null -> {
                    when (it.flag) {
                        0 -> whenLikeOrCommentSet()
                        1 -> whenPostReload(it.data as PostsData)
                        -1, -2 -> whenPostDelete()
                    }
                    it.data = null
                }
            }
        }


        // set new comment
        binding.tvSend.setOnClickListener {
            if (binding.txtComment.text.toString().isNotEmpty()) {
                viewModel.commentRequest = CommentRequest(
                    comment = binding.txtComment.text.toString(),
                    postId = currentPostsData.id!!
                )
                binding.txtComment.isEnabled = false
                binding.tvSend.isClickable = false
                binding.pbCommentLoad.visibility = View.VISIBLE
                binding.txtComment.setText("")

                viewModel.setComment()
                requireActivity().crToast(
                    getString(R.string.comment_set_success),
                    ToastType.SUCCESS
                )

            }
        }


    }

    private fun whenPostReload(postsData: PostsData) {
        loadPostData(postsData)
        if (usersDialog!!.isAdded) {
            requireActivity().crToast(getString(R.string.post_delete_success), ToastType.INFO)
            usersDialog!!.dismiss()
        }
        preferences.updateSomeData(UpdateData(whereData = postsData))
    }

    private fun whenLikeOrCommentSet() {
        viewModel.postById(currentPostsData.id.toString())
    }

    private fun whenPostDelete() {
        requireActivity().crToast(getString(R.string.post_delete_success), ToastType.INFO)
        requireActivity().navigateToActivity(MainActivity::class.java)
    }

    @SuppressLint("SetTextI18n")
    private fun loadPostData(newDPostData: PostsData) {
        handleLoading(binding.loading, false)

        binding.ivProfile.loadWebImage(newDPostData.user!!.avatar + "", isProfile = true)
        binding.tvName.text = newDPostData.user.name
        binding.tvDate.text = newDPostData.createdAt!!.setTimeFormat(requireContext())

        // editable for owner user

        //post details
        binding.tvTitle.text = newDPostData.title
        binding.tvDescription.text = newDPostData.content
        binding.tvCommentNum.text = newDPostData.commentsCount
        binding.tvLikeNum.text = newDPostData.likesCount

        //set images
        newDPostData.let {
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

            binding.clImage.setOnClickListener { _ ->
                requireActivity().startActivity(
                    Intent(context, PlayerActivity::class.java)
                        .putExtra("postData", it)
                )

            }

            //show video
            binding.vVideo.setOnClickListener { _ ->
                requireActivity().startActivity(
                    Intent(context, PlayerActivity::class.java)
                        .putExtra("videoUrl", it.video)
                )
            }
        }


        //show video
        binding.vVideo.setOnClickListener {
            requireActivity().startActivity(
                Intent(context, PlayerActivity::class.java)
                    .putExtra("videoUrl", newDPostData.video)
            )
        }

        binding.tvCommentNum.setOnClickListener {
            showCommentDialog(newDPostData)
        }
        binding.tvLikeNum.setOnClickListener {
            showLikeDialog(newDPostData)
        }

        binding.ivSetLike.setOnClickListener {
            if (!preferences.isGustUser()) {
                binding.txtComment.isEnabled = false
                binding.tvSend.isClickable = false
                if (newDPostData.isLike != 1) {
                    newDPostData.isLike = 1
                    binding.ivSetLike.setImageResource(R.drawable.ic_like_fill)
                    binding.tvLikeNum.text = (newDPostData.likesCount!!.toInt() + 1).toString()
                    viewModel.likePostRequest = LikePostRequest(postId = newDPostData.id!!)
                    viewModel.setLike()
                } else {
                    binding.ivSetLike.setImageResource(R.drawable.ic_heart)
                    newDPostData.isLike = 0
                    binding.tvLikeNum.text = (newDPostData.likesCount!!.toInt() - 1).toString()
                    viewModel.likePostRequest = LikePostRequest(postId = newDPostData.id!!)
                    viewModel.removeLike()
                }
            } else {
                requireActivity().askLogin()
            }


        }

        if (newDPostData.isLike == 1) {
            binding.ivSetLike.setImageResource(R.drawable.ic_like_fill)
        } else {
            binding.ivSetLike.setImageResource(R.drawable.ic_heart)
        }

    }


    private fun showCommentDialog(data: PostsData) {
        usersDialog!!.setCommentList(data, true).show(parentFragmentManager, "comment")
    }

    private fun showLikeDialog(data: PostsData) {
        usersDialog!!.setLikeList(data).show(parentFragmentManager, "comment")
    }


}