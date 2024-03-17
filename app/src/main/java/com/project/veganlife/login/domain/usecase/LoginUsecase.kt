package com.project.veganlife.login.domain.usecase

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.project.veganlife.login.data.model.LoginRequest
import com.project.veganlife.login.data.model.LoginResponse
import com.project.veganlife.login.domain.repository.LoginRepository
import javax.inject.Inject

class LoginUsecase @Inject constructor(val loginRepository: LoginRepository) {
    suspend operator fun invoke(loginProvider: String, context: Context): LoginResponse? {
        val sdkAccessTokenResult = loginRepository.login(loginProvider)
        val apiResponseResult = loginRepository.loginApi(loginProvider, LoginRequest(sdkAccessTokenResult))

        // data store에 저장
        val prefFileName = "encrypted_prefs"
        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

        val sharedPreferences = EncryptedSharedPreferences.create(
            prefFileName,
            masterKeyAlias,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

        sharedPreferences.edit().apply{
            if (apiResponseResult != null) {
                putString("ApiAccessToken",apiResponseResult.accessToken)
                putString("ApiRefreshToken",apiResponseResult.refreshToken)
            }
        }.apply()

        return apiResponseResult
    }
}