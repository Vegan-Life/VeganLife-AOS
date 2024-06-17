package com.project.veganlife.lifecheck.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.project.veganlife.databinding.ItemRecyclerviewLifecheckMenuSearchBinding
import com.project.veganlife.lifecheck.data.model.LifeCheckMealData

class LifeCheckMealDataAdapter :
    PagingDataAdapter<LifeCheckMealData, LifeCheckMealDataAdapter.MealDataViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealDataViewHolder {
        val binding = ItemRecyclerviewLifecheckMenuSearchBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MealDataViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MealDataViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        }
    }

    class MealDataViewHolder(private val binding: ItemRecyclerviewLifecheckMenuSearchBinding) :
        RecyclerView.ViewHolder(binding.root) {
                
        fun bind(mealData: LifeCheckMealData) {
            binding.tvLifecheckMenuSearchWord.text = mealData.name
        }
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<LifeCheckMealData>() {
            override fun areItemsTheSame(
                oldItem: LifeCheckMealData,
                newItem: LifeCheckMealData
            ): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: LifeCheckMealData,
                newItem: LifeCheckMealData
            ): Boolean =
                oldItem == newItem
        }
    }
}