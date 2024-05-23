package com.project.veganlife.login.data.local

import com.project.veganlife.login.data.model.LoginResponse

interface LocalUserDataSource {
    suspend fun saveToken(provider: String, token: LoginResponse)
}