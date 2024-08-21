package com.project.veganlife.signup.data.repositoryImpl

import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.data.model.ProfileResponse
import com.project.veganlife.signup.data.datasource.SignupAddInfoRemoteDataSource
import com.project.veganlife.signup.domain.SignupAddInfoRepository
import okhttp3.RequestBody
import javax.inject.Inject

class SignupAddInfoRepositoryImpl @Inject constructor(
    private val signupAddInfoRemoteDataSource: SignupAddInfoRemoteDataSource,
) : SignupAddInfoRepository {
    override suspend fun signupAddInfo(signupRequestDTO: RequestBody): ApiResult<ProfileResponse> {
        return signupAddInfoRemoteDataSource.signupAddInfo(signupRequestDTO)
    }
}