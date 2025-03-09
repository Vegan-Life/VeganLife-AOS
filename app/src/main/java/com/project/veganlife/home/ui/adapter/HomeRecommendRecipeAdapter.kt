package com.project.veganlife.home.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.TextViewCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.project.veganlife.R
import com.project.veganlife.databinding.ItemRecyclerviewHomeRecommendBinding
import com.project.veganlife.databinding.ItemRecyclerviewRecipeListBinding
import com.project.veganlife.recipe.data.model.RecipeFeedContent
import com.project.veganlife.utils.ui.VeganTypeChange.Companion.changeBackground
import com.project.veganlife.utils.ui.VeganTypeChange.Companion.changeVeganType
import com.project.veganlife.utils.ui.VeganTypeChange.Companion.changeVeganTypeTextColor

class HomeRecommendRecipeAdapter(
    private val recipeFeedItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<HomeRecommendRecipeAdapter.RecipeFeedsViewHolder>() {
    interface OnItemClickListener {
        fun onItemCLicked(item: RecipeFeedContent)
    }

    private val items = mutableListOf<RecipeFeedContent>()

    inner class RecipeFeedsViewHolder(private val binding: ItemRecyclerviewHomeRecommendBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: RecipeFeedContent) {
            binding.apply {
                Glide.with(itemView)
                    .load(item.thumbnailUrl)
                    .apply(
                        RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .fitCenter()
                            .placeholder(R.color.sub_gray2) // 로드 전 기본 이미지/색상
                            .error(R.color.sub_gray2) // 로딩 실패 시 기본 색상
                    ).into(ivHomeThumbnail)

                tvHomeTitle.text = item.recipeTitle

                val typeOne = item.recipeTypes.getOrNull(0)
                val typeTwo = item.recipeTypes.getOrNull(1)

                typeOne?.let {
                    tvHomeAbleVeganTypeOne.apply {
                        text = changeVeganType(it)
                        setTextColor(ContextCompat.getColor(itemView.context, changeVeganTypeTextColor(it)))
                        setBackgroundResource(changeBackground(it))
                    }
                }

                typeTwo?.let {
                    tvHomeAbleVeganTypeTwo.apply {
                        text = changeVeganType(it)
                        setTextColor(ContextCompat.getColor(itemView.context, changeVeganTypeTextColor(it)))
                        setBackgroundResource(changeBackground(it))
                        visibility = View.VISIBLE
                    }
                } ?: run {
                    tvHomeAbleVeganTypeTwo.visibility = View.GONE
                }

                root.setOnClickListener {
                    recipeFeedItemClickListener.onItemCLicked(item)
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecipeFeedsViewHolder {
        val binding = ItemRecyclerviewHomeRecommendBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return RecipeFeedsViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: RecipeFeedsViewHolder, position: Int) {
         holder.bind(items[position])
    }

    fun submitList(newItems: List<RecipeFeedContent>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
}