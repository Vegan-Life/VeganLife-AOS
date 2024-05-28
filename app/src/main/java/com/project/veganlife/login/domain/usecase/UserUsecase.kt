package com.project.veganlife.login.domain.usecase

import com.project.veganlife.login.data.model.LoginResponse
import com.project.veganlife.login.domain.repository.UserRepository
import javax.inject.Inject

class UserUsecase @Inject constructor(val userRepository: UserRepository) {
    suspend operator fun invoke(provider: String, token: LoginResponse) {
        userRepository.saveToken(provider, token)
    }
}