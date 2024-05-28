package com.project.veganlife.community.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.veganlife.community.data.model.Feed
import com.project.veganlife.databinding.ItemRecyclerviewCommunityHomeFeedBinding

class FeedsAdapter : ListAdapter<Feed, FeedsAdapter.FeedsViewHolder>(diffUtil) {
    inner class FeedsViewHolder(private val binding: ItemRecyclerviewCommunityHomeFeedBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Feed) {
            binding.apply {
                if (item.imageUrl != null) {
                    Glide.with(itemView)
                        .load(item.imageUrl)
                        .into(ivCommunityhomefeed)
                }
                tvCommunityhomefeedTitle.text = item.title
                tvCommunityhomefeedDatetime.text = item.content
                tvCommunityhomefeedDatetime.text = item.createdAt
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): FeedsViewHolder {
        val itemCommunityHomeFeedBinding = ItemRecyclerviewCommunityHomeFeedBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false,
        )

        return FeedsViewHolder(itemCommunityHomeFeedBinding)
    }

    override fun onBindViewHolder(
        holder: FeedsViewHolder,
        position: Int,
    ) {
        holder.bind(currentList[position])
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Feed>() {
            override fun areItemsTheSame(
                oldItem: Feed,
                newItem: Feed,
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: Feed,
                newItem: Feed,
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
