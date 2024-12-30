package com.project.veganlife.lifecheck.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.project.veganlife.databinding.ItemRecyclerviewLifecheckMenuSearchBinding
import com.project.veganlife.lifecheck.data.model.LifeCheckMealData

class LifeCheckMealDataAdapter(
    private val listener: OnItemLongClickListener
) : PagingDataAdapter<LifeCheckMealData, LifeCheckMealDataAdapter.MealDataViewHolder>(diffUtil) {

    private var isLongClickEnabled: Boolean = false

    // 롱클릭 활성화/비활성화 설정
    fun setLongClickEnabled(enabled: Boolean) {
        isLongClickEnabled = enabled
    }

    // 롱클릭 이벤트를 위한 인터페이스
    interface OnItemLongClickListener {
        fun onItemLongClicked(id: Long)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealDataViewHolder {
        val binding = ItemRecyclerviewLifecheckMenuSearchBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MealDataViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: MealDataViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item, isLongClickEnabled)
        }
    }

    class MealDataViewHolder(
        private val binding: ItemRecyclerviewLifecheckMenuSearchBinding,
        private val listener: OnItemLongClickListener
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(mealData: LifeCheckMealData, isLongClickEnabled: Boolean) {
            binding.tvLifecheckMenuSearchWord.text = mealData.name

            if (isLongClickEnabled) {
                // 롱클릭 이벤트 활성화
                binding.root.setOnLongClickListener {
                    listener.onItemLongClicked(mealData.id.toLong()) // ID 전달
                    true
                }
            } else {
                // 롱클릭 이벤트 비활성화
                binding.root.setOnLongClickListener(null)
            }
        }
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<LifeCheckMealData>() {
            override fun areItemsTheSame(
                oldItem: LifeCheckMealData,
                newItem: LifeCheckMealData
            ): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: LifeCheckMealData,
                newItem: LifeCheckMealData
            ): Boolean = oldItem == newItem
        }
    }
}