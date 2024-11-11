package com.project.veganlife.recipe.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.project.veganlife.R
import com.project.veganlife.databinding.ItemRecyclerviewRecipeListBinding
import com.project.veganlife.recipe.data.model.RecipeFeedContent
import com.project.veganlife.utils.ui.VeganTypeChange

class RecipeHomeAdapter(
    private val recipeFeedItemClickListener: OnItemClickListener
) : PagingDataAdapter<RecipeFeedContent, RecipeHomeAdapter.RecipeFeedsViewHolder>(diffUtil) {
    interface OnItemClickListener {
        fun onItemCLicked(item: RecipeFeedContent)
    }

    inner class RecipeFeedsViewHolder(private val binding: ItemRecyclerviewRecipeListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: RecipeFeedContent) {
            binding.apply {
                if (item.thumbnailUrl != null) {
                    Glide.with(itemView)
                        .load(item.thumbnailUrl)
                        .apply(
                            RequestOptions()
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .fitCenter()
                                .placeholder(R.color.sub_gray2) // 로드 전 기본 이미지/색상
                                .error(R.color.sub_gray2) // 로딩 실패 시 기본 색상
                        ).into(ivRecipeThumbnail)
                }

                tvRecipeName.text = item.recipeTitle
                tvRecipeNickname.text = item.author.nickname
                tvRecipeVeganType.text = VeganTypeChange.changeVeganType(item.author.vegetarianType)

                when (item.recipeTypes.size) {
                    1 -> {
                        tvRecipeAbleVeganTypeOne.text =
                            VeganTypeChange.changeVeganType(item.recipeTypes.get(0))
                    }

                    2 -> {
                        tvRecipeAbleVeganTypeOne.text =
                            VeganTypeChange.changeVeganType(item.recipeTypes.get(0))
                        tvRecipeAbleVeganTypeTwo.text =
                            VeganTypeChange.changeVeganType(item.recipeTypes.get(1))
                    }
                }


                root.setOnClickListener {
                    recipeFeedItemClickListener.onItemCLicked(item)
                }
            }
        }

        private fun updateLikeBackground(isLike: Boolean) {
            binding.apply {
                if (isLike) {
                    btnRecipeLike.setImageResource(R.drawable.all_like_full_recipe)
                } else {
                    btnRecipeLike.setImageResource(R.drawable.all_like_empty_recipe)
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecipeFeedsViewHolder {
        val binding = ItemRecyclerviewRecipeListBinding.inflate(
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
        val diffUtil = object : DiffUtil.ItemCallback<RecipeFeedContent>() {
            override fun areItemsTheSame(
                oldItem: RecipeFeedContent,
                newItem: RecipeFeedContent
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: RecipeFeedContent,
                newItem: RecipeFeedContent
            ): Boolean {
                return oldItem == newItem
            }

        }
    }
}