package com.project.veganlife.community.ui.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.project.veganlife.R
import com.project.veganlife.community.data.model.PostPreview
import com.project.veganlife.databinding.ItemRecyclerviewCommunityHomeFeedBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
class CommunityFeedAdapter(
    private val feedItemClickListener: OnItemClickListener
) : PagingDataAdapter<PostPreview, CommunityFeedAdapter.FeedsViewHolder>(diffUtil) {
    interface OnItemClickListener {
        fun onItemClicked(item: PostPreview)
    }
    inner class FeedsViewHolder(private val binding: ItemRecyclerviewCommunityHomeFeedBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PostPreview) {
            binding.apply {
                if (item.imageUrl != null) {
                    Glide.with(itemView)
                        .load(item.imageUrl)
                        .apply(RequestOptions().error(R.drawable.all_spoon_fork_small))
                        .fitCenter()
                        .into(ivCommunityhomefeed)
                }
                tvCommunityhomefeedTitle.text = item.title
                tvCommunityhomefeedDescription.text = item.content
                tvCommunityhomefeedDatetime.text = parseDateTime(item.createdAt)

                root.setOnClickListener {
                    feedItemClickListener.onItemClicked(item)
                }

            }
        }


        private fun parseDateTime(input: String): String {
            // 마이크로초 부분 제거 ('.' 이후 부분 제거)
            val trimmedInput = input.substringBefore('.')

            // 입력 문자열을 LocalDateTime으로 파싱 (마이크로초 제외)
            val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
            val dateTime = LocalDateTime.parse(trimmedInput, inputFormatter)

            // 원하는 형식으로 포맷 (한국어 로케일 설정)
            val outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd a hh:mm", Locale("ko", "KR"))
            return dateTime.format(outputFormatter)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): FeedsViewHolder {
        val itemCommunityHomeFeedBinding = ItemRecyclerviewCommunityHomeFeedBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false,
        )

        return FeedsViewHolder(itemCommunityHomeFeedBinding)
    }

    override fun onBindViewHolder(
        holder: FeedsViewHolder,
        position: Int,
    ) {
        getItem(position)?.let { holder.bind(it) }
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<PostPreview>() {
            override fun areItemsTheSame(
                oldItem: PostPreview,
                newItem: PostPreview,
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: PostPreview,
                newItem: PostPreview,
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
