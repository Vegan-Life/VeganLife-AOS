package com.project.veganlife.recipe.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.project.veganlife.databinding.ItemRecyclerviewExpandablelayoutIngredientsBinding

class RecipeDetailIngredientAdapter(
) : ListAdapter<String, RecipeDetailIngredientAdapter.RecipeFeedsViewHolder>(diffUtil) {
    inner class RecipeFeedsViewHolder(private val binding: ItemRecyclerviewExpandablelayoutIngredientsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String) {
            binding.tvRecipeRecipe.text = item
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecipeFeedsViewHolder {
        val binding = ItemRecyclerviewExpandablelayoutIngredientsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RecipeFeedsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecipeFeedsViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(
                oldItem: String,
                newItem: String
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: String,
                newItem: String
            ): Boolean {
                return oldItem == newItem
            }

        }
    }
}