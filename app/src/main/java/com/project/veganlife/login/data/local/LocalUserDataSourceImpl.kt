package com.project.veganlife.login.data.local

import android.content.SharedPreferences
import com.project.veganlife.login.data.model.LoginResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LocalUserDataSourceImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences,
) : LocalUserDataSource {
    override suspend fun saveToken(provider: String, token: LoginResponse) {
        withContext(Dispatchers.IO) {
            sharedPreferences.edit().apply {
                putString("provider", provider)
                putString("ApiAccessToken", token.accessToken)
                putString("ApiRefreshToken", token.refreshToken)
            }.commit()
        }
    }
}