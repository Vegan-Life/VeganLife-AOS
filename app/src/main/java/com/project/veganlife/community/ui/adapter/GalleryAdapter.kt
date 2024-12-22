package com.project.veganlife.community.ui.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.project.veganlife.databinding.ItemRecyclerviewCommunityWriteEditFeedPhotoBinding

class GalleryAdapter(private val onDeleteClicked: (Int) -> Unit) : ListAdapter<Uri, GalleryAdapter.GalleryViewHolder>(diffUtil) {
    inner class GalleryViewHolder(private val binding: ItemRecyclerviewCommunityWriteEditFeedPhotoBinding) :
        ViewHolder(binding.root) {
        fun bind(uri: Uri) {
            Glide.with(binding.root)
                .load(uri)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(binding.ivCommunityWriteEditFeedPhoto)

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
        val diffUtil = object : DiffUtil.ItemCallback<Uri>() {
            override fun areItemsTheSame(oldItem: Uri, newItem: Uri): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Uri, newItem: Uri): Boolean {
                return oldItem.toString() == newItem.toString()
            }
        }
    }

}
