package com.project.veganlife.recipe.ui.adapter

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.project.veganlife.databinding.ItemRecyclerviewRecipeDescriptionBinding

class RecipeWriteDescriptionAdapter(
    private val onDeleteClick: (Int) -> Unit,
) : ListAdapter<String, RecipeWriteDescriptionAdapter.DescriptionViewHolder>(diffUtil) {
    inner class DescriptionViewHolder(private val binding: ItemRecyclerviewRecipeDescriptionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String, position: Int, isFirstItem: Boolean) {
            binding.tvRecipeDescriptionNumber.text = String.format("%02d", position + 1)

            // EditText에 아이템 데이터 반영
            binding.etRecipeDescription.setText(item)
            if (isFirstItem) {
                // 첫 번째 아이템이면 x 버튼 숨기기
                binding.btnRecipeDescriptionDelete.visibility = View.INVISIBLE
            } else {
                // 첫 번째가 아니면 x 버튼 표시 및 클릭 리스너 설정
                binding.btnRecipeDescriptionDelete.visibility = View.VISIBLE
                binding.btnRecipeDescriptionDelete.setOnClickListener {
                    onDeleteClick(position)
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): DescriptionViewHolder {
        val binding = ItemRecyclerviewRecipeDescriptionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return DescriptionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DescriptionViewHolder, position: Int) {
        // 첫 번째 아이템인지 확인하여 bind 메서드에 전달
        val isFirstItem = position == 0
        holder.bind(getItem(position), position, isFirstItem)
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