package com.project.veganlife.mypage.data.model

data class MypageModifyRequestDTO(
    val nickname: String,
    val vegetarianType: String,
    val gender: String,
    val birthYear: Int,
    val height: Int,
    val weight: Int,
    val existingImageUrl: String?
)