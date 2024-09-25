package com.project.veganlife.data.datasource

import android.content.SharedPreferences
import javax.inject.Inject


class TokenManagerImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences
): TokenManager {

    override fun getAccessToken(): String? {
        return sharedPreferences.getString("ApiAccessToken", "")
    }

    override fun getRefreshToken(): String? {
        return sharedPreferences.getString("ApiRefreshToken", "")
    }

    override fun saveTokens(accessToken: String, refreshToken: String) {
        sharedPreferences.edit().apply {
            putString("ApiAccessToken", accessToken)
            putString("ApiRefreshToken", refreshToken)
        }.apply()
    }
}
