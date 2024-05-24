package com.project.veganlife.community.data.model

data class Feed(
    val id: Long,
    val title: String,
    val content: String,
    val imageUrl: String?,
    val createdAt: String,
)
