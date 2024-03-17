package com.project.veganlife.login.domain.usecase

import android.content.Context
import com.project.veganlife.login.data.model.LoginRequest
import com.project.veganlife.login.data.model.LoginResponse
import com.project.veganlife.login.domain.repository.LoginRepository
import javax.inject.Inject

class LoginUsecase @Inject constructor(val loginRepository: LoginRepository) {
    suspend operator fun invoke(loginProvider: String, context: Context): LoginResponse? {
        val sdkAccessTokenResult = loginRepository.login(loginProvider)
        val apiResponseResult = loginRepository.loginApi(loginProvider, LoginRequest(sdkAccessTokenResult))

        return apiResponseResult
    }
}