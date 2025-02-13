package com.project.veganlife.lifecheck.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.project.veganlife.databinding.ItemRecyclerviewLifecheckDietDetailBinding
import com.project.veganlife.lifecheck.data.model.LifeCheckMealDataDetail

class LifeCheckDietDetailAdapter :
    ListAdapter<LifeCheckMealDataDetail, LifeCheckDietDetailAdapter.MealViewHolder>(MealDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealViewHolder {
        val binding = ItemRecyclerviewLifecheckDietDetailBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return MealViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MealViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class MealViewHolder(private val binding: ItemRecyclerviewLifecheckDietDetailBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(meal: LifeCheckMealDataDetail) {
            binding.tvLifecheckDietDetailFood.text = meal.name
            binding.tvLifecheckDietDetailOneServing.text =
                "1회제공량 (${meal.amountPerServe} ${meal.intakeUnit.lowercase()})"
            binding.tvLifecheckDietDetailValue.text = meal.amount.toString()
            binding.tvLifecheckDietDetailUnit.text = meal.intakeUnit.lowercase()
        }
    }

    class MealDiffCallback : DiffUtil.ItemCallback<LifeCheckMealDataDetail>() {
        override fun areItemsTheSame(
            oldItem: LifeCheckMealDataDetail,
            newItem: LifeCheckMealDataDetail
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: LifeCheckMealDataDetail,
            newItem: LifeCheckMealDataDetail
        ): Boolean {
            return oldItem == newItem
        }
    }
}
