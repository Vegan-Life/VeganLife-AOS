package com.project.veganlife.login.data.local

import android.content.SharedPreferences
import com.project.veganlife.login.data.model.LoginResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import javax.inject.Inject

class LocalUserDataSourceImpl @Inject constructor(
    private var sharedPreferences: SharedPreferences,
) : LocalUserDataSource {
    override suspend fun saveToken(token: LoginResponse) {

        val saveToken = CoroutineScope(Dispatchers.IO).async {
            sharedPreferences.edit().apply {
                putString("ApiAccessToken", token.accessToken)
                putString("ApiRefreshToken", token.refreshToken)
            }.commit()
        }

        saveToken.await()
    }
}