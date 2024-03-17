package com.project.veganlife.login.data.model

data class LoginResponse (
    val hasAdditionalInfo: Boolean,
    val email: String,
    val accessToken: String,
    val refreshToken: String
)
