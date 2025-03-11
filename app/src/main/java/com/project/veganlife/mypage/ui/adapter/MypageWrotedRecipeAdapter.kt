package com.project.veganlife.mypage.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.project.veganlife.R
import com.project.veganlife.databinding.ItemRecyclerviewRecipeListBinding
import com.project.veganlife.recipe.data.model.RecipeFeedContent
import com.project.veganlife.utils.ui.VeganTypeChange.Companion.changeBackground
import com.project.veganlife.utils.ui.VeganTypeChange.Companion.changeVeganType
import com.project.veganlife.utils.ui.VeganTypeChange.Companion.changeVeganTypeTextColor

class MypageWrotedRecipeAdapter(
    private val mypageWrotedRecipeItemClickListener: OnItemClickListener
) : PagingDataAdapter<RecipeFeedContent, MypageWrotedRecipeAdapter.MypageWrotedRecipeViewHolder>(diffUtil) {
    interface OnItemClickListener {
        fun onItemCLicked(item: RecipeFeedContent)
    }

    inner class MypageWrotedRecipeViewHolder(private val binding: ItemRecyclerviewRecipeListBinding) :
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
                    ).into(ivRecipeThumbnail)

                tvRecipeName.text = item.recipeTitle
                tvRecipeNickname.text = item.author.nickname

                tvRecipeVeganType.apply {
                    text = changeVeganType(item.author.vegetarianType)
                    setTextColor(ContextCompat.getColor(itemView.context, changeVeganTypeTextColor(item.author.vegetarianType)))
                    setBackgroundResource(changeBackground(item.author.vegetarianType))
                }

                val typeOne = item.recipeTypes.getOrNull(0)
                val typeTwo = item.recipeTypes.getOrNull(1)

                typeOne?.let {
                    tvRecipeAbleVeganTypeOne.apply {
                        text = changeVeganType(it)
                        setTextColor(ContextCompat.getColor(itemView.context, changeVeganTypeTextColor(it)))
                        setBackgroundResource(changeBackground(it))
                    }
                }

                if(typeTwo != null) {
                    typeTwo.let {
                        tvRecipeAbleVeganTypeTwo.apply {
                            text = changeVeganType(it)
                            setTextColor(ContextCompat.getColor(itemView.context, changeVeganTypeTextColor(it)))
                            setBackgroundResource(changeBackground(it))
                            visibility = View.VISIBLE
                        }
                    }
                } else {
                    tvRecipeAbleVeganTypeTwo.visibility = View.INVISIBLE
                }

                root.setOnClickListener {
                    mypageWrotedRecipeItemClickListener.onItemCLicked(item)
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MypageWrotedRecipeViewHolder {
        val binding = ItemRecyclerviewRecipeListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MypageWrotedRecipeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MypageWrotedRecipeViewHolder, position: Int) {
        val postedFeed = getItem(position)
        if (postedFeed != null) {
            holder.bind(postedFeed)
        } else {
            Log.d("Adapter", "Bind position: $position, Data: null")
        }
    }


    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<RecipeFeedContent>() {
            override fun areItemsTheSame(
                oldItem: RecipeFeedContent,
                newItem: RecipeFeedContent
            ): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: RecipeFeedContent,
                newItem: RecipeFeedContent
            ): Boolean =
                oldItem == newItem
        }
    }
}
