package com.project.veganlife.signup.domain

import com.project.veganlife.signup.data.model.SignupRequest
import javax.inject.Inject

class SignupAddInfoUsecase @Inject constructor(
    val signupAddInfoRepository: SignupAddInfoRepository,
){
    suspend operator fun invoke(signupRequest: SignupRequest): String? {
        return signupAddInfoRepository.setSignup(signupRequest)
    }
}