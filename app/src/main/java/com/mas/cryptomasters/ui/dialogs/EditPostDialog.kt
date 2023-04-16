package com.mas.cryptomasters.ui.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import com.mas.cryptomasters.databinding.EditPostDialogBinding
import com.mas.cryptomasters.utils.PostsActions

class EditPostDialog(context: Context) : Dialog(context) {

    private lateinit var binding: EditPostDialogBinding
    private lateinit var postsActions: PostsActions
    private var isOwner: Boolean = false
    private var isGuest: Boolean = false
    private var isFromComment: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = EditPostDialogBinding.inflate(layoutInflater)
        window?.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))

        setContentView(binding.root)


        binding.tvEdit.setOnClickListener {
            dismiss()
            postsActions.onEdit()
        }

        binding.tvDelete.setOnClickListener {
            dismiss()
            postsActions.onDelete()
        }

        binding.tvReport.setOnClickListener {
            dismiss()
            postsActions.onReport()
        }

        binding.tvBlock.setOnClickListener {
            dismiss()
            postsActions.onBlock()
        }
    }

    fun setPostAction(postsActions: PostsActions): EditPostDialog {
        this.postsActions = postsActions
        return this
    }

    fun setSettings(
        isOwner: Boolean = false,
        isFromComment: Boolean = false,
        isGuest: Boolean = false
    ): EditPostDialog {
        this.isOwner = isOwner
        this.isFromComment = isFromComment
        this.isGuest = isGuest
        return this
    }

    override fun onStart() {
        super.onStart()
        val show = View.VISIBLE
        val hide = View.GONE
        when{
            isGuest -> setViewsVisibility(block = hide, report = show, edit = hide, delete = hide)
            isOwner -> setViewsVisibility(block = hide, report = show, edit = show, delete = show)
            !isOwner && !isGuest -> setViewsVisibility(block = show, report = show, edit = hide, delete = hide)
        }

        if (isFromComment || isGuest || isOwner)
            binding.tvBlock.visibility = View.GONE
        else
            binding.tvBlock.visibility = View.VISIBLE

    }

    private fun setViewsVisibility(block: Int, report: Int, edit: Int, delete: Int) {
        binding.tvBlock.visibility = block
        binding.tvReport.visibility = report
        binding.tvEdit.visibility =edit
        binding.tvDelete.visibility = delete
    }


}