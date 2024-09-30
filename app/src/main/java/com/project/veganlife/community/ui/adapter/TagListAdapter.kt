package com.project.veganlife.community.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.project.veganlife.databinding.ItemRecyclerviewCommunitySearchPopularityTagBinding

class TagListAdapter : ListAdapter<String, TagListAdapter.TagViewHolder>(diffUtil) {
    inner class TagViewHolder(private val binding: ItemRecyclerviewCommunitySearchPopularityTagBinding) :
        ViewHolder(binding.root) {
        fun bind(item: String) {
            binding.tvCommunitySearchPopularityTag.text = item
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagViewHolder {
        return TagViewHolder(
            ItemRecyclerviewCommunitySearchPopularityTagBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: TagViewHolder, position: Int) {
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