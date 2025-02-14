package com.project.veganlife.lifecheck.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.project.veganlife.R
import com.project.veganlife.databinding.ItemRecyclerviewLifecheckHomeDietBinding
import com.project.veganlife.lifecheck.data.model.LifeCheckMealLogListResponse

class LifeCheckMealLogListAdapter(
    private val onItemClick: (Long) -> Unit
) :
    ListAdapter<LifeCheckMealLogListResponse, LifeCheckMealLogListAdapter.MealLogViewHolder>(MealLogDiffCallback()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealLogViewHolder {
        val binding = ItemRecyclerviewLifecheckHomeDietBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return MealLogViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MealLogViewHolder, position: Int) {
        val mealLog = getItem(position)
        holder.bind(mealLog)
    }

    inner class MealLogViewHolder(private val binding: ItemRecyclerviewLifecheckHomeDietBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(mealLog: LifeCheckMealLogListResponse) {
            binding.apply {
                tvLifecheckhomedietKcal.text = "${mealLog.totalCalorie} kcal"

                tvLifecheckhomedietTag.text = when (mealLog.mealType) {
                    "BREAKFAST" -> root.context.getString(R.string.lifecheck_morning)
                    "LUNCH" -> root.context.getString(R.string.lifecheck_noon)
                    "DINNER" -> root.context.getString(R.string.lifecheck_evening)
                    "BREAKFAST_SNACK" -> root.context.getString(R.string.lifecheck_snack)
                    "LUNCH_SNACK" -> root.context.getString(R.string.lifecheck_snack)
                    "DINNER_SNACK" -> root.context.getString(R.string.lifecheck_snack)
                    else -> "기타"
                }

                if (!mealLog.thumbnailUrl.isNullOrEmpty()) {
                    Glide.with(ivLifecheckhomediet.context)
                        .load(mealLog.thumbnailUrl)
                        .placeholder(R.drawable.all_spoon_fork_small)
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(ivLifecheckhomediet)
                } else {
                    Glide.with(ivLifecheckhomediet.context)
                        .load(R.drawable.all_logo_gray)
                        .placeholder(R.drawable.all_logo_gray)
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(ivLifecheckhomediet)
                }

                root.setOnClickListener {
                    onItemClick(mealLog.id)
                }
            }
        }
    }

    class MealLogDiffCallback : DiffUtil.ItemCallback<LifeCheckMealLogListResponse>() {
        override fun areItemsTheSame(
            oldItem: LifeCheckMealLogListResponse,
            newItem: LifeCheckMealLogListResponse
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: LifeCheckMealLogListResponse,
            newItem: LifeCheckMealLogListResponse
        ): Boolean {
            return oldItem == newItem
        }
    }
}