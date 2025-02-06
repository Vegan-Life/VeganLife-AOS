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
import com.project.veganlife.recipe.data.model.RecipeWriteIngredient

class RecipeWriteIngredientAdapter(
    private val onDeleteClick: (Int) -> Unit,
    private val onTextChange: (Int, String) -> Unit // 텍스트 변경 콜백 추가
) : ListAdapter<RecipeWriteIngredient, RecipeWriteIngredientAdapter.IngredientViewHolder>(diffUtil) {
    inner class IngredientViewHolder(private val binding: ItemRecyclerviewRecipeIngredientBinding) :
        RecyclerView.ViewHolder(binding.root) {
            init {
                // EditText의 텍스트 변경 사항을 콜백으로 전달
                binding.etRecipeIngredient.addTextChangedListener(object : TextWatcher {

                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        val position = bindingAdapterPosition
                        // 변경된 값을 콜백으로 전달
                        if(position != RecyclerView.NO_POSITION && position < currentList.size) onTextChange(bindingAdapterPosition, s.toString())

                    }
                    override fun afterTextChanged(s: Editable?) {}
                })

                binding.btnRecipeIngredientDelete.setOnClickListener {
                    val position = bindingAdapterPosition
                    if(position != RecyclerView.NO_POSITION && position < currentList.size) onDeleteClick(bindingAdapterPosition)
                }
            }

        fun bind(item: RecipeWriteIngredient) {
            binding.etRecipeIngredient.setText(item.ingredient)
            // 첫 번째 아이템이면 x 버튼 숨기기
            binding.btnRecipeIngredientDelete.visibility = if(bindingAdapterPosition == 0) View.INVISIBLE else View.VISIBLE
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
        holder.bind(getItem(position))
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<RecipeWriteIngredient>() {
            override fun areItemsTheSame(
                oldItem: RecipeWriteIngredient,
                newItem: RecipeWriteIngredient
            ): Boolean {
                return oldItem.number == newItem.number
            }

            override fun areContentsTheSame(
                oldItem: RecipeWriteIngredient,
                newItem: RecipeWriteIngredient
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
