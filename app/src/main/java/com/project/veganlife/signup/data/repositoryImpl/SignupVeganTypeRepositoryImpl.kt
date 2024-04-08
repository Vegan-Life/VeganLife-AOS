package com.project.veganlife.signup.data.repositoryImpl

import com.project.veganlife.signup.data.local.LocalSignupDataSource
import com.project.veganlife.signup.data.model.SignupVeganType
import com.project.veganlife.signup.domain.SignupVeganTypeRepository
import javax.inject.Inject

class SignupVeganTypeRepositoryImpl @Inject constructor(
    private val signupVeganTypeDataSource: LocalSignupDataSource,
): SignupVeganTypeRepository {
    override suspend fun saveVeganTypeList(): List<SignupVeganType> {
        return signupVeganTypeDataSource.saveVeganTypeList()
    }
}