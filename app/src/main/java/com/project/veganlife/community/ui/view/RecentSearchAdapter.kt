package com.project.veganlife.community.ui.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.project.veganlife.databinding.ItemRecyclerviewCommunitySearchRecentBinding

class RecentSearchAdapter :
    ListAdapter<String, RecentSearchAdapter.RecentSearchViewHolder>(diffUtil) {
    inner class RecentSearchViewHolder(val binding: ItemRecyclerviewCommunitySearchRecentBinding) :
        RecyclerView.ViewHolder(binding.root) {

            fun bind(item: String) {
                binding.tvCommunitySearchRecentSearchTerms.text = item
            }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentSearchViewHolder {
        return RecentSearchViewHolder(
            ItemRecyclerviewCommunitySearchRecentBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecentSearchViewHolder, position: Int) {
        getItem(position).let { holder.bind(it) }
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
