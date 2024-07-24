package com.project.veganlife.community.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.project.veganlife.databinding.ItemRecyclerviewCommunitySearchPopularityTagBinding

class PopularTagAdapter : ListAdapter<String, PopularTagAdapter.PopularTagViewHolder>(diffUtil) {
    inner class PopularTagViewHolder(private val binding: ItemRecyclerviewCommunitySearchPopularityTagBinding) :
        ViewHolder(binding.root) {
        fun bind(item: String) {
            binding.tvCommunitySearchPopularityTag.text = item
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularTagViewHolder {
        return PopularTagViewHolder(
            ItemRecyclerviewCommunitySearchPopularityTagBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: PopularTagViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(
                oldItem: String,
                newItem: String,
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: String,
                newItem: String,
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}