package com.project.veganlife.mypage.domain.usecase

import com.project.veganlife.login.domain.repository.LoginRepository
import javax.inject.Inject

class MypageLogoutUsecase @Inject constructor(
    val loginRepository: LoginRepository,
    ) {
    suspend operator fun invoke(provider: String): String {
        return loginRepository.logout(provider)
    }
}