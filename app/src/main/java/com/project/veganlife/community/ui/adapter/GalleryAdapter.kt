package com.project.veganlife.community.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.project.veganlife.community.data.model.ImageItem
import com.project.veganlife.databinding.ItemRecyclerviewCommunityWriteEditFeedPhotoBinding

class GalleryAdapter(private val onDeleteClicked: (Int) -> Unit) :
    ListAdapter<ImageItem, GalleryAdapter.GalleryViewHolder>(diffUtil) {
    inner class GalleryViewHolder(private val binding: ItemRecyclerviewCommunityWriteEditFeedPhotoBinding) :
        ViewHolder(binding.root) {
        fun bind(imageItem: ImageItem) {
            if (imageItem.url != null) {
                Glide.with(binding.root)
                    .load(imageItem.url)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(binding.ivCommunityWriteEditFeedPhoto)
            } else if (imageItem.uri != null) {
//                Glide.with(binding.root)
//                    .load(imageItem.uri)
//                    .centerCrop()
//                    .diskCacheStrategy(DiskCacheStrategy.NONE)
//                    .into(binding.ivCommunityWriteEditFeedPhoto)
                binding.ivCommunityWriteEditFeedPhoto.setImageURI(imageItem.uri)
            }


            binding.ibCommunityWriteEditFeedPhotoDelete.setOnClickListener {
                onDeleteClicked(bindingAdapterPosition)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
        return GalleryViewHolder(
            ItemRecyclerviewCommunityWriteEditFeedPhotoBinding.inflate(LayoutInflater.from(parent.context))
        )
    }

    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<ImageItem>() {
            override fun areItemsTheSame(oldItem: ImageItem, newItem: ImageItem): Boolean {
                return oldItem.url == newItem.url || oldItem.uri == newItem.uri
            }

            override fun areContentsTheSame(oldItem: ImageItem, newItem: ImageItem): Boolean {
                return oldItem == newItem
            }
        }
    }

}
