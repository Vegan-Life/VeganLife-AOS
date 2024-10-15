package com.project.veganlife.alarm.ui.adapter

import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.project.veganlife.R
import com.project.veganlife.alarm.data.model.AlarmContent
import com.project.veganlife.databinding.ItemRecyclerviewAlarmBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class AlarmAdapter :
    PagingDataAdapter<AlarmContent, AlarmAdapter.AlarmListItemViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmListItemViewHolder {
        val binding = ItemRecyclerviewAlarmBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AlarmListItemViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: AlarmListItemViewHolder, position: Int) {
        val alarmContent = getItem(position)
        if (alarmContent != null) {
            holder.bind(alarmContent)
        } else {
            Log.d("Adapter", "Bind position: $position, Data: null")
        }
    }

    class AlarmListItemViewHolder(private val binding: ItemRecyclerviewAlarmBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.O)
        private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd a hh:mm", Locale.KOREAN)

        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(alarmContent: AlarmContent) {
            binding.apply {
                tvAlarmMessage.text = alarmContent.message

                if (!alarmContent.createdAt.isNullOrEmpty()) {
                    try {
                        val parsedDate = LocalDateTime.parse(alarmContent.createdAt)
                        tvAlarmDate.text = parsedDate.format(formatter)
                    } catch (e: Exception) {
                        Log.e("AlarmAdapter", "Failed to parse date: ${alarmContent.createdAt}", e)
                    }
                }

                // 시간 가공 + 타입에 맞는 이미지 넣기
                if (!alarmContent.type.isNullOrEmpty()) {
                    Glide.with(binding.root)
                        .load(typeProcess(alarmContent.type))
                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                        .into(ivAlarmImage)
                }
            }
        }

        private fun typeProcess(type: String): Int {
            return when (type) {
                "INTAKE_OVER_30" -> R.drawable.alarm_warning
                "INTAKE_OVER_60" -> R.drawable.alarm_dangers
                "COMMENT" -> R.drawable.alarm_message
                "COMMENT_LIKE" -> R.drawable.alarm_message
                "MENTION" -> R.drawable.alarm_message
                else -> R.drawable.alarm_basic
            }
        }
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<AlarmContent>() {
            override fun areItemsTheSame(
                oldItem: AlarmContent,
                newItem: AlarmContent
            ): Boolean =
                oldItem.createdAt == newItem.createdAt

            override fun areContentsTheSame(
                oldItem: AlarmContent,
                newItem: AlarmContent
            ): Boolean =
                oldItem == newItem
        }
    }
}