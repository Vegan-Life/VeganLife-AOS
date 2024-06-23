package com.project.veganlife.mypage.data.model

data class ProfileModifyRequest(
    val nickname: String,
    val imageUrl: String,
    val vegetarianType: String,
    val gender: String,
    val birthYear: Int,
    val height: Int,
    val weight: Int,
)