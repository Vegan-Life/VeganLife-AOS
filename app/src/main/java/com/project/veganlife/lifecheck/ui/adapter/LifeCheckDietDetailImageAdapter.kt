package com.project.veganlife.lifecheck.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.project.veganlife.R
import com.project.veganlife.databinding.ItemImageBinding

class LifeCheckDietDetailImageAdapter :
    ListAdapter<String, LifeCheckDietDetailImageAdapter.ViewPagerViewHolder>(ImageDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerViewHolder {
        val binding = ItemImageBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewPagerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewPagerViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun submitList(list: List<String>?) {
        val photoList =
            if (list.isNullOrEmpty()) {
                listOf("null_image")
            } else {
                list
            }
        super.submitList(photoList)
    }

    class ViewPagerViewHolder(private val binding: ItemImageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(imageUrl: String) {
            if (imageUrl == "null_image") {
                Glide.with(binding.imageView1.context)
                    .load(R.drawable.all_logo_gray)
                    .placeholder(R.drawable.all_logo_gray)
                    .error(R.color.sub_gray2)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(binding.imageView1)
            } else {
                Glide.with(binding.imageView1.context)
                    .load(imageUrl)
                    .placeholder(R.drawable.all_logo_gray) // 로드 전 기본 이미지
                    .error(R.color.sub_gray2) // 로딩 실패 시 기본 색상
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(binding.imageView1)
            }
        }
    }

    class ImageDiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }
}
