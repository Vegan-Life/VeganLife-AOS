package com.project.veganlife.community.data.model

data class Post(
    val id: Long,
    val author: String,
    val vegetarianType: String,
    val profileImageUrl: String,
    val title: String,
    val content: String,
    val createdAt: String,
    val isLike: Boolean,
    val likeCount: Long,
    val commentCount: Long,
    val imageUrls: List<String>,
    val tags: List<String>,
    val comments: List<Comment>
)

