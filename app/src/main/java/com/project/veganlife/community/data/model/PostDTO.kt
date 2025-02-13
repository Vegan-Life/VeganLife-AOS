package com.project.veganlife.community.data.model

data class PostDTO(
    val title: String,
    val content: String,
    val tags: List<String>,
)

data class PostUpdateDTO(
    val title: String,
    val content: String,
    val tags: List<String>,
    val existingImageUrls: List<String>
)
