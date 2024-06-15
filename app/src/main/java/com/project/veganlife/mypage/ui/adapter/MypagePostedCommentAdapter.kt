package com.project.veganlife.mypage.ui.adapter

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.project.veganlife.R
import com.project.veganlife.databinding.ItemRecyclerviewMypagePostedBinding
import com.project.veganlife.mypage.data.model.MyPostedContent

class MypagePostedCommentAdapter :
    PagingDataAdapter<MyPostedContent, MypagePostedCommentAdapter.PostedFeedViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostedFeedViewHolder {
        val binding = ItemRecyclerviewMypagePostedBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PostedFeedViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostedFeedViewHolder, position: Int) {
        val postedFeed = getItem(position)
        if (postedFeed != null) {
            holder.bind(postedFeed)
        } else {
            Log.d("Adapter", "Bind position: $position, Data: null")
        }
    }

    class PostedFeedViewHolder(private val binding: ItemRecyclerviewMypagePostedBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(postedFeed: MyPostedContent) {
            binding.apply {
                tvMypageTitle.text = postedFeed.title
                tvMypageDatetime.text = postedFeed.createdAt
                tvMypageDescription.text = postedFeed.content

                if (!postedFeed.imageUrl.isNullOrEmpty()) {
                    Glide.with(binding.root)
                        .load(postedFeed.imageUrl)
                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                        .into(ivMypageThumbnail)
                } else {
                    binding.ivMypageThumbnail.setImageResource(R.color.sub_gray1)
                }

                clMypageLayout.setOnClickListener {
                    val bundle = Bundle()
                    bundle.putInt("feedId", postedFeed.id)
                    itemView.findNavController().navigate(
                        R.id.action_mypagePostedCommentsFragment_to_communityDetailFeedFragment,
                        bundle
                    )
                }
            }
        }
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<MyPostedContent>() {
            override fun areItemsTheSame(
                oldItem: MyPostedContent,
                newItem: MyPostedContent
            ): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: MyPostedContent,
                newItem: MyPostedContent
            ): Boolean =
                oldItem == newItem
        }
    }
}
