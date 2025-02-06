package com.project.veganlife.lifecheck.ui.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.project.veganlife.R
import com.project.veganlife.databinding.ItemRecyclerviewLifecheckDietAddPhotoBinding

class LifeCheckDietAddAdapter(
    private val onPhotoDelete: (Uri) -> Unit
) : ListAdapter<Uri, LifeCheckDietAddAdapter.PhotoViewHolder>(PhotoDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val binding = ItemRecyclerviewLifecheckDietAddPhotoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PhotoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val photoUri = getItem(position)
        holder.bind(photoUri)
    }

    inner class PhotoViewHolder(
        private val binding: ItemRecyclerviewLifecheckDietAddPhotoBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(photoUri: Uri) {
            Glide.with(binding.ivLifecheckDietAddPhoto.context)
                .load(photoUri)
                .placeholder(R.drawable.all_spoon_fork_small)
                .error(R.color.sub_gray2)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(binding.ivLifecheckDietAddPhoto)

            binding.ibLifecheckDietAddPhotoDelete.setOnClickListener {
                onPhotoDelete(photoUri)
            }
        }
    }

    private class PhotoDiffCallback : DiffUtil.ItemCallback<Uri>() {
        override fun areItemsTheSame(oldItem: Uri, newItem: Uri): Boolean = oldItem == newItem
        override fun areContentsTheSame(oldItem: Uri, newItem: Uri): Boolean = oldItem == newItem
    }
}