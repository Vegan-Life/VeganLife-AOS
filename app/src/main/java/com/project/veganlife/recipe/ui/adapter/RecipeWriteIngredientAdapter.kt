package com.project.veganlife.recipe.ui.adapter

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.project.veganlife.databinding.ItemRecyclerviewRecipeIngredientBinding

class RecipeWriteIngredientAdapter(
    private val onDeleteClick: (Int) -> Unit,
    private val onTextChange: (Int, String) -> Unit // 텍스트 변경 콜백 추가
) : ListAdapter<String, RecipeWriteIngredientAdapter.IngredientViewHolder>(diffUtil) {
    inner class IngredientViewHolder(private val binding: ItemRecyclerviewRecipeIngredientBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String) {
            // EditText의 텍스트 변경 사항을 콜백으로 전달
            binding.etRecipeIngredient.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    onTextChange(bindingAdapterPosition, s.toString()) // 변경된 값을 콜백으로 전달
                }
                override fun afterTextChanged(s: Editable?) {}
            })

            if (bindingAdapterPosition == 0) {
                // 첫 번째 아이템이면 x 버튼 숨기기
                binding.btnRecipeIngredientDelete.visibility = View.INVISIBLE
            } else {
                // 첫 번째가 아니면 x 버튼 표시 및 클릭 리스너 설정
                binding.btnRecipeIngredientDelete.visibility = View.VISIBLE
                binding.btnRecipeIngredientDelete.setOnClickListener {
                    onDeleteClick(bindingAdapterPosition)
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): IngredientViewHolder {
        val binding = ItemRecyclerviewRecipeIngredientBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return IngredientViewHolder(binding)
    }

    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
        // 첫 번째 아이템인지 확인하여 bind 메서드에 전달
        val isFirstItem = position == 0
        holder.bind(getItem(position))
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
