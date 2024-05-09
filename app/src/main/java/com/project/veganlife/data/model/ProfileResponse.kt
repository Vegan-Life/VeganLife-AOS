package com.project.veganlife.data.model

data class ProfileResponse(
    val email: String,
    val nickname: String,
    val vegetarianType: String,
    val gender: String,
    val imageUrl: String,
    val birthYear: Int,
    val height: Int,
    val weight: Int,
)
