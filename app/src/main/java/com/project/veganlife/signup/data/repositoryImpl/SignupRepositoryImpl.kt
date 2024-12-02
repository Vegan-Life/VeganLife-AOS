package com.project.veganlife.signup.data.repositoryImpl

import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.data.model.ProfileResponse
import com.project.veganlife.signup.data.datasource.SignupAddInfoRemoteDataSourceImpl
import com.project.veganlife.signup.data.local.LocalSignupDataSourceImpl
import com.project.veganlife.signup.data.model.SignupVeganType
import com.project.veganlife.signup.domain.repository.SignupRepository
import okhttp3.RequestBody
import javax.inject.Inject

class SignupRepositoryImpl @Inject constructor(
    private val signupAddInfoRemoteDataSourceImpl: SignupAddInfoRemoteDataSourceImpl,
    private val localSignupDataSourceImpl: LocalSignupDataSourceImpl
): SignupRepository {
    override suspend fun signupAddInfo(signupRequestDTO: RequestBody): ApiResult<ProfileResponse> {
        return signupAddInfoRemoteDataSourceImpl.signupAddInfo(signupRequestDTO)
    }

    override suspend fun saveVeganTypeList(): List<SignupVeganType> {
        return localSignupDataSourceImpl.saveVeganTypeList()
    }
}