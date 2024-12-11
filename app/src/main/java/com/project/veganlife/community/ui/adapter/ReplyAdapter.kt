package com.project.veganlife.community.ui.adapter

import android.os.Build
import android.text.Spannable
import android.text.SpannableString
import android.text.style.TextAppearanceSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.project.veganlife.R
import com.project.veganlife.community.data.model.Comment
import com.project.veganlife.databinding.ItemRecyclerviewCommunityDetailFeedCommentsReplyBinding
import com.project.veganlife.utils.formatDateTime

@RequiresApi(Build.VERSION_CODES.O)
class ReplyAdapter: ListAdapter<Comment, ReplyAdapter.ReplyViewHolder>(diffUtil) {
    inner class ReplyViewHolder(private val binding: ItemRecyclerviewCommunityDetailFeedCommentsReplyBinding) :
        ViewHolder(binding.root) {
        fun bind(item: Comment) {
            binding.apply {
                tvCommunityDetailFeedCommentsNickname.text = item.author
                tvCommunityDetailFeedCommentsReplyTime.text = formatDateTime(item.createdAt)

                val words = item.content.split(" ")
                if (words[0].contains("@")) {
                    val tag = words[0]
                    val rest = item.content.subSequence(tag.length, item.content.length)

                    val spannableString = SpannableString(tag)

                    val textAppearanceSpan =
                        TextAppearanceSpan(binding.root.context, R.style.CommentTag)
                    spannableString.setSpan(
                        textAppearanceSpan,
                        0, tag.length,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )

                    tvCommunityDetailFeedCommentsReplyDescription.text = spannableString
                    tvCommunityDetailFeedCommentsReplyDescription.append(rest)
                } else {
                    tvCommunityDetailFeedCommentsReplyDescription.text = item.content
                }
            }

        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReplyViewHolder {
        return ReplyViewHolder(
            ItemRecyclerviewCommunityDetailFeedCommentsReplyBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ReplyViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Comment>() {
            override fun areItemsTheSame(
                oldItem: Comment,
                newItem: Comment,
            ): Boolean {
                return oldItem == newItem
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
