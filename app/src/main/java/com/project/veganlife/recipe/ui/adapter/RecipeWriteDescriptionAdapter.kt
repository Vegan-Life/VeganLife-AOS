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
import com.project.veganlife.recipe.data.model.RecipeWriteDescription

class RecipeWriteDescriptionAdapter(
    private val onDeleteClick: (Int) -> Unit,
    private val onTextChange: (Int, String) -> Unit // 텍스트 변경 콜백 추가
) : ListAdapter<RecipeWriteDescription, RecipeWriteDescriptionAdapter.DescriptionViewHolder>(diffUtil) {
    inner class DescriptionViewHolder(private val binding: ItemRecyclerviewRecipeDescriptionBinding) :
        RecyclerView.ViewHolder(binding.root) {
            init {
                // EditText의 텍스트 변경 사항을 콜백으로 전달
                binding.etRecipeDescription.addTextChangedListener(object : TextWatcher {

                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        val position = bindingAdapterPosition
                        // 변경된 값을 콜백으로 전달
                        if(position != RecyclerView.NO_POSITION && position < currentList.size) onTextChange(bindingAdapterPosition, s.toString())
                    }
                    override fun afterTextChanged(s: Editable?) {}
                })
                binding.btnRecipeDescriptionDelete.setOnClickListener {
                    val position = bindingAdapterPosition
                    if(position != RecyclerView.NO_POSITION && position < currentList.size) onDeleteClick(bindingAdapterPosition)
                }
            }

        fun bind(item: RecipeWriteDescription, position: Int) {

            binding.tvRecipeDescriptionNumber.text = String.format("%02d", position + 1)

            // EditText에 아이템 데이터 반영
            binding.etRecipeDescription.setText(item.description)
            binding.btnRecipeDescriptionDelete.visibility = if(bindingAdapterPosition == 0) View.INVISIBLE else View.VISIBLE
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
        holder.bind(getItem(position), position)
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<RecipeWriteDescription>() {
            override fun areItemsTheSame(
                oldItem: RecipeWriteDescription,
                newItem: RecipeWriteDescription
            ): Boolean {
                return oldItem.number == newItem.number
            }

            override fun areContentsTheSame(
                oldItem: RecipeWriteDescription,
                newItem: RecipeWriteDescription
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}