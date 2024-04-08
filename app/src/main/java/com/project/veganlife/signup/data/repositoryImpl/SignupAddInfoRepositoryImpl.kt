package com.project.veganlife.signup.data.repositoryImpl

import com.project.veganlife.signup.data.datasource.SignupAddInfoDataSource
import com.project.veganlife.signup.data.model.SignupRequest
import com.project.veganlife.signup.domain.SignupAddInfoRepository
import javax.inject.Inject

class SignupAddInfoRepositoryImpl @Inject constructor(
    private val signupAddInfoDataSource: SignupAddInfoDataSource,
) : SignupAddInfoRepository {
    override suspend fun setSignup(signupRequest: SignupRequest): String? {
        return signupAddInfoDataSource.setSignup(signupRequest)
    }
}