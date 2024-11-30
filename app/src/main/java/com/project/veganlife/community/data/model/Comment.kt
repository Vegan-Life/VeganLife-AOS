package com.project.veganlife.community.data.model

data class Comment(
    val id: Long,
    val author: String,
    val content: String,
    val createdAt: String,
    var subComments: List<Comment>? = null
)