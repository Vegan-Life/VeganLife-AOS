package com.project.veganlife.signup.domain

import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.data.model.ProfileResponse
import okhttp3.RequestBody

interface SignupAddInfoRepository {
    suspend fun signupAddInfo(
        signupRequestDTO: RequestBody,
    ): ApiResult<ProfileResponse>
}