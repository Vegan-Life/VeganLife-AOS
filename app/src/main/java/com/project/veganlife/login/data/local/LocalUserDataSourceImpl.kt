package com.project.veganlife.login.data.local

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.project.veganlife.login.data.model.LoginResponse
import dagger.hilt.android.qualifiers.ApplicationContext

class LocalUserDataSourceImpl(
    @ApplicationContext private val context: Context,
) : LocalUserDataSource {
    override suspend fun saveToken(token: LoginResponse) {

        val prefFileName = "encrypted_prefs"
        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

        val sharedPreferences = EncryptedSharedPreferences.create(
            prefFileName,
            masterKeyAlias,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

        sharedPreferences.edit().apply {
            putString("ApiAccessToken", token.accessToken)
            putString("ApiRefreshToken", token.refreshToken)
        }.apply()

    }
}