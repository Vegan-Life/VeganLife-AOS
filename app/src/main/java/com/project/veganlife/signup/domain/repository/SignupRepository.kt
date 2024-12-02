package com.project.veganlife.signup.domain.repository

import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.data.model.ProfileResponse
import com.project.veganlife.signup.data.model.SignupVeganType
import okhttp3.RequestBody

interface SignupRepository {
    suspend fun signupAddInfo(signupRequestDTO: RequestBody): ApiResult<ProfileResponse>

    suspend fun saveVeganTypeList(): List<SignupVeganType>
}