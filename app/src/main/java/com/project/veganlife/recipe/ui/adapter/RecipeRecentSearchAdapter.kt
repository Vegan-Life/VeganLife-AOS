package com.project.veganlife.recipe.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.project.veganlife.databinding.ItemRecyclerviewRecipeSearchRecentBinding

class RecipeRecentSearchAdapter(
    private val onDeleteClick: (Int) -> Unit,
    private val recipeRecentSearchItemClickListener: OnItemClickListener
) :
    ListAdapter<String, RecipeRecentSearchAdapter.RecipeRecentSearchHolder>(diffUtil) {
    interface OnItemClickListener {
        fun onItemCLicked(item: String)
    }

    inner class RecipeRecentSearchHolder(val binding: ItemRecyclerviewRecipeSearchRecentBinding) :
        RecyclerView.ViewHolder(binding.root) {

            init {
                binding.ibRecipeSearchRecentDelete.setOnClickListener {
                    val position = bindingAdapterPosition
                    if(position != RecyclerView.NO_POSITION && position < currentList.size) onDeleteClick(bindingAdapterPosition)
                }
            }

        fun bind(item: String) {
            binding.tvRecipeSearchRecentSearchTerms.text = item
            binding.tvRecipeSearchRecentSearchTerms.setOnClickListener {
                recipeRecentSearchItemClickListener.onItemCLicked(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeRecentSearchHolder {
        return RecipeRecentSearchHolder(
            ItemRecyclerviewRecipeSearchRecentBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecipeRecentSearchHolder, position: Int) {
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
