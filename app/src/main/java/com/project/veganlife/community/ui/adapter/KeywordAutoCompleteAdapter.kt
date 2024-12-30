package com.project.veganlife.community.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.project.veganlife.databinding.ItemRecyclerviewCommunityWriteEditKeywordAutoCompleteBinding

class KeywordAutoCompleteAdapter(val onTagClicked: (String) -> Unit) : ListAdapter<String, KeywordAutoCompleteAdapter.KeywordViewHolder>(diffUtil) {
    inner class KeywordViewHolder(private val binding: ItemRecyclerviewCommunityWriteEditKeywordAutoCompleteBinding) :
        ViewHolder(binding.root) {
        fun bind(item: String) {
            binding.tvAutoComplete.text = item

            binding.root.setOnClickListener {
                onTagClicked(item)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KeywordViewHolder {
        return KeywordViewHolder(
            ItemRecyclerviewCommunityWriteEditKeywordAutoCompleteBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: KeywordViewHolder, position: Int) {
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