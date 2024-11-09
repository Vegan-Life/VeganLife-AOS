package com.project.veganlife.community.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.project.veganlife.R
import com.project.veganlife.databinding.ItemImageBinding

class PostImagesViewPagerAdapter :
    ListAdapter<String, PostImagesViewPagerAdapter.ViewPagerViewHolder>(diffUtil) {

    inner class ViewPagerViewHolder(private val binding: ItemImageBinding) :
        RecyclerView.ViewHolder(binding.root) {
            fun bind(url: String) {
                // Glide로 이미지 로드
                Glide.with(binding.imageView1.context)
                    .load(url)
                    .placeholder(R.drawable.all_spoon_fork_small) // 로드 전 기본 이미지
                    .error(R.color.sub_gray2) // 로딩 실패 시 기본 색상
                    .fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(binding.imageView1)

            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerViewHolder {
        val view = ItemImageBinding.inflate(
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
        val diffUtil = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(
                oldItem: String,
                newItem: String,
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: String,
                newItem: String,
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}