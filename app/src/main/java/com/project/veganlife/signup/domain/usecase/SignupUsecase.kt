package com.project.veganlife.signup.domain.usecase

import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.data.model.ProfileResponse
import com.project.veganlife.signup.data.model.SignupVeganType
import com.project.veganlife.signup.domain.repository.SignupRepository
import okhttp3.RequestBody
import javax.inject.Inject

class SignupUsecase @Inject constructor(
    private val signupRepository: SignupRepository
) {
    suspend fun signupAddInfo(signupRequestDTO: RequestBody): ApiResult<ProfileResponse> {
        return signupRepository.signupAddInfo(signupRequestDTO)
    }
    suspend fun saveVeganTypeList(): List<SignupVeganType> {
        return signupRepository.saveVeganTypeList()
    }

}