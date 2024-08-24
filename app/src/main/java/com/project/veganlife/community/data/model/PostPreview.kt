package com.project.veganlife.community.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PostPreview(
    val id: Long,
    val title: String,
    val content: String,
    val imageUrl: String?,
    val createdAt: String,
): Parcelable
