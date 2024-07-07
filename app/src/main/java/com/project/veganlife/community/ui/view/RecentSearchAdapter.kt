package com.project.veganlife.community.ui.view

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.project.veganlife.databinding.ItemRecyclerviewCommunitySearchRecentBinding

class RecentSearchAdapter: ListAdapter<String, RecentSearchAdapter.RecentSearchViewHolder>(diffUtil) {
    inner class RecentSearchViewHolder(val binding: ItemRecyclerviewCommunitySearchRecentBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentSearchViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: RecentSearchViewHolder, position: Int) {
        TODO("Not yet implemented")
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
