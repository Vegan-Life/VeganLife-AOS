package com.project.veganlife.login.data.local

import com.project.veganlife.login.data.model.LoginResponse

interface LocalUserDataSource {
    suspend fun saveToken(token: LoginResponse)
}