package com.project.veganlife.signup.domain

import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.data.model.ProfileResponse
import okhttp3.RequestBody
import javax.inject.Inject

class SignupAddInfoUsecase @Inject constructor(
    val signupAddInfoRepository: SignupAddInfoRepository,
) {
    suspend operator fun invoke(signupRequestDTO: RequestBody): ApiResult<ProfileResponse> {
        return signupAddInfoRepository.signupAddInfo(signupRequestDTO)
    }
}