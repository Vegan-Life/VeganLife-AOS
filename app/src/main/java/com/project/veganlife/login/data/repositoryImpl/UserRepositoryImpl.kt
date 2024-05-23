package com.project.veganlife.login.data.repositoryImpl

import com.project.veganlife.login.data.local.LocalUserDataSource
import com.project.veganlife.login.data.model.LoginResponse
import com.project.veganlife.login.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val localUserDataSource: LocalUserDataSource,
) : UserRepository {
    override suspend fun saveToken(provider: String, token: LoginResponse) {
        localUserDataSource.saveToken(provider, token)
    }
}