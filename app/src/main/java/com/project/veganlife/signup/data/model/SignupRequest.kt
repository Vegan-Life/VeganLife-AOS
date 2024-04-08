package com.project.veganlife.signup.data.model

data class SignupRequest(
    val nickname: String,
    val gender: String,
    val vegetarianType: String,
    val birthYear: Int,
    val height: Int,
    val weight: Int
)
