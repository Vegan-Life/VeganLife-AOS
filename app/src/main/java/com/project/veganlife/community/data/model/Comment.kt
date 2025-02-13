package com.project.veganlife.community.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Comment(
    val id: Long,
    val author: String,
    val content: String,
    val createdAt: String,
    var subComments: List<Comment>? = null
): Parcelable