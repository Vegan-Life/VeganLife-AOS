package com.project.veganlife.login.domain.repository

import com.project.veganlife.login.data.model.LoginResponse

interface UserRepository {
    suspend fun saveToken(provider: String, token: LoginResponse)

}