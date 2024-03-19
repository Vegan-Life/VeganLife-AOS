package com.project.veganlife.login.domain.repository

import com.project.veganlife.login.data.model.LoginRequest
import com.project.veganlife.login.data.model.LoginResponse

interface LoginRepository {
    suspend fun loginApi(
        provider: String,
        accessToken: LoginRequest?,
    ): LoginResponse?

    suspend fun login(
        provider: String,
    ): String
}