package com.project.veganlife.mypage.ui.adapter

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.project.veganlife.R
import com.project.veganlife.databinding.ItemRecyclerviewRecipeListBinding
import com.project.veganlife.mypage.data.model.ScrapedRecipeContent
import com.project.veganlife.mypage.ui.viewmodel.MypageScrapedRecipeViewModel
import com.project.veganlife.utils.ui.VeganTypeChange

class MypageScrapedRecipeAdapter(private val viewModel: MypageScrapedRecipeViewModel) :
    PagingDataAdapter<ScrapedRecipeContent, MypageScrapedRecipeAdapter.ScrapedRecipeViewHolder>(
        diffUtil
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScrapedRecipeViewHolder {
        val binding = ItemRecyclerviewRecipeListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ScrapedRecipeViewHolder(binding, viewModel)
    }

    override fun onBindViewHolder(holder: ScrapedRecipeViewHolder, position: Int) {
        val postedFeed = getItem(position)
        if (postedFeed != null) {
            holder.bind(postedFeed)
        } else {
            Log.d("Adapter", "Bind position: $position, Data: null")
        }
    }

    class ScrapedRecipeViewHolder(
        private val binding: ItemRecyclerviewRecipeListBinding,
        private val viewModel: MypageScrapedRecipeViewModel
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(scrapedRecipe: ScrapedRecipeContent) {
            binding.apply {
                tvRecipeName.text = scrapedRecipe.recipeName
                tvRecipeNickname.text = scrapedRecipe.author.nickname
                tvRecipeVeganType.text =
                    VeganTypeChange.changeVeganType(scrapedRecipe.author.vegetarianType)

                tvRecipeAbleVeganTypeOne.text =
                    VeganTypeChange.changeVeganType(scrapedRecipe.recipeTypes.get(0))

                if (scrapedRecipe.recipeTypes.size == 2) {
                    tvRecipeAbleVeganTypeTwo.text =
                        VeganTypeChange.changeVeganType(scrapedRecipe.recipeTypes.get(1))
                } else {
                    tvRecipeAbleVeganTypeTwo.visibility = View.INVISIBLE
                }

                Glide.with(binding.root)
                    .load(scrapedRecipe.thumbnailUrl)
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                    .into(ivRecipeThumbnail)

                updateLikeBackground(scrapedRecipe.isLiked)

                btnRecipeLike.setOnClickListener {
                    viewModel.like_likeCancelRecipe(scrapedRecipe)
                }

                clMypageLayout.setOnClickListener {
                    val bundle = Bundle()
                    bundle.putLong("recipeId", scrapedRecipe.recipeId)
                    itemView.findNavController().navigate(
                        R.id.action_mypageScrapsRecipeFragment_to_recipeDetailInfoFragment,
                        bundle
                    )
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

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<ScrapedRecipeContent>() {
            override fun areItemsTheSame(
                oldItem: ScrapedRecipeContent,
                newItem: ScrapedRecipeContent
            ): Boolean =
                oldItem.recipeId == newItem.recipeId

            override fun areContentsTheSame(
                oldItem: ScrapedRecipeContent,
                newItem: ScrapedRecipeContent
            ): Boolean =
                oldItem == newItem
        }
    }
}
