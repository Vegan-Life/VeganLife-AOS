package com.project.veganlife.signup.data.repositoryImpl

import com.project.veganlife.signup.data.datasource.SignupAddInfoRemoteDataSource
import com.project.veganlife.signup.data.model.SignupRequest
import com.project.veganlife.signup.domain.SignupAddInfoRepository
import javax.inject.Inject

class SignupAddInfoRepositoryImpl @Inject constructor(
    private val signupAddInfoRemoteDataSource: SignupAddInfoRemoteDataSource,
) : SignupAddInfoRepository {
    override suspend fun setSignup(signupRequest: SignupRequest): String? {
        return signupAddInfoRemoteDataSource.setSignup(signupRequest)
    }
}