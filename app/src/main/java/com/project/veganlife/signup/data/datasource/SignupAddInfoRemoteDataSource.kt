package com.project.veganlife.signup.data.datasource

import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.data.model.ProfileResponse
import okhttp3.RequestBody

interface SignupAddInfoRemoteDataSource {
    suspend fun signupAddInfo(
        signupRequestDTO: RequestBody,
    ): ApiResult<ProfileResponse>
}