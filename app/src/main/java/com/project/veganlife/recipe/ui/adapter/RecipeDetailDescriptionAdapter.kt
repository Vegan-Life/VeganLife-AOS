package com.project.veganlife.recipe.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.project.veganlife.databinding.ItemRecyclerviewExpandablelayoutDescriptionsBinding
import com.project.veganlife.recipe.data.model.RecipeDetailDescription

class RecipeDetailDescriptionAdapter(
) : ListAdapter<RecipeDetailDescription, RecipeDetailDescriptionAdapter.RecipeFeedsViewHolder>(diffUtil) {
    inner class RecipeFeedsViewHolder(private val binding: ItemRecyclerviewExpandablelayoutDescriptionsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: RecipeDetailDescription) {
            // 순서 번호 표시
            binding.ivRecipeDescriptionNumber.text = item.number.toString()
            // 설명 텍스트 표시
            binding.tvRecipeRecipe.text = item.description
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecipeFeedsViewHolder {
        val binding = ItemRecyclerviewExpandablelayoutDescriptionsBinding.inflate(
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
        val diffUtil = object : DiffUtil.ItemCallback<RecipeDetailDescription>() {
            override fun areItemsTheSame(
                oldItem: RecipeDetailDescription,
                newItem: RecipeDetailDescription
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: RecipeDetailDescription,
                newItem: RecipeDetailDescription
            ): Boolean {
                return oldItem == newItem
            }

        }
    }
}