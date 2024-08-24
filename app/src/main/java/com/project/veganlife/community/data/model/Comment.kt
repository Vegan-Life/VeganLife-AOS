package com.project.veganlife.community.data.model

data class Comment(
    val id: Long,
    val author: String,
    val content: String,
    val isLike: Boolean,
    val likeCount: Long,
    val createdAt: String,
    val subComments: List<Comment>? = null
)