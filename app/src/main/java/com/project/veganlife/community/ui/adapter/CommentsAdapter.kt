package com.project.veganlife.community.ui.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.project.veganlife.community.data.model.Comment
import com.project.veganlife.databinding.ItemRecyclerviewCommunityDetailFeedCommentsBinding
import com.project.veganlife.utils.formatDateTime

@RequiresApi(Build.VERSION_CODES.O)
class CommentsAdapter(
    private val buttonClickListener: OnReplyCommentClickListener?
) : ListAdapter<Comment, CommentsAdapter.CommentsViewHolder>(diffUtil) {
    inner class CommentsViewHolder(private val binding: ItemRecyclerviewCommunityDetailFeedCommentsBinding) :
        ViewHolder(binding.root) {
        fun bind(item: Comment) {
            binding.apply {
                tvCommunityDetailFeedCommentsNickname.text = item.author
                tvCommunityDetailFeedCommentsTime.text = formatDateTime(item.createdAt)
                tvCommunityDetailFeedCommentsDescription.text = item.content
            }


            binding.ibCommunityDetailFeedCommentsReply.setOnClickListener {
                buttonClickListener?.onButtonClick(it, item)
            }

            val replayAdapter = ReplyAdapter()
            binding.rvCommentReply.adapter = replayAdapter
            replayAdapter.submitList(item.subComments)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentsViewHolder {
        return CommentsViewHolder(
            ItemRecyclerviewCommunityDetailFeedCommentsBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: CommentsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Comment>() {
            override fun areItemsTheSame(
                oldItem: Comment,
                newItem: Comment,
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: Comment,
                newItem: Comment,
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}

interface OnReplyCommentClickListener {
    fun onButtonClick(view: View, item: Comment)
}
