package com.project.veganlife.recipe.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.ObjectKey
import com.project.veganlife.R
import com.project.veganlife.databinding.ItemRecyclerviewRecipeWriteEditFeedPhotoBinding
import com.project.veganlife.recipe.data.model.RecipeWriteFeedPhoto

class RecipeWriteImagesViewPagerAdapter(
    private val onDeleteClick: (Int) -> Unit,
) :
    ListAdapter<RecipeWriteFeedPhoto, RecipeWriteImagesViewPagerAdapter.ViewPagerViewHolder>(
        diffUtil
    ) {
    inner class ViewPagerViewHolder(private val binding: ItemRecyclerviewRecipeWriteEditFeedPhotoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.ibRecipeWriteEditFeedPhotoDelete.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) onDeleteClick(position)
            }
        }

        fun bind(url: RecipeWriteFeedPhoto) {
            Glide.with(binding.ivRecipeWriteEditFeedPhoto.context)
                .load(url.photo)
                .apply(
                    RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.NONE) // 디스크 캐시 사용 안 함
                        .skipMemoryCache(true) // 메모리 캐시 사용 안 함
                        .placeholder(R.drawable.all_spoon_fork_small) // 기본 이미지
                        .error(R.drawable.all_spoon_fork_small) // 로드 실패 시 기본 이미지
                        .fitCenter() // 이미지 가운데를 기준으로 자르기
                        .signature(ObjectKey(System.currentTimeMillis().toString())) // 매번 새로 로드
                )
                .into(binding.ivRecipeWriteEditFeedPhoto)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerViewHolder {
        val view = ItemRecyclerviewRecipeWriteEditFeedPhotoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewPagerViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewPagerViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<RecipeWriteFeedPhoto>() {
            override fun areItemsTheSame(
                oldItem: RecipeWriteFeedPhoto,
                newItem: RecipeWriteFeedPhoto,
            ): Boolean {
                return oldItem.number == newItem.number
            }

            override fun areContentsTheSame(
                oldItem: RecipeWriteFeedPhoto,
                newItem: RecipeWriteFeedPhoto,
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}