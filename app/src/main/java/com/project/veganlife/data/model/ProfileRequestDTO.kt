package com.project.veganlife.data.model

data class ProfileRequestDTO(
    val nickname: String,
    val vegetarianType: String,
    val gender: String,
    val birthYear: Int,
    val height: Int,
    val weight: Int,
)