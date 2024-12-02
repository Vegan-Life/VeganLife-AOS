package com.project.veganlife.login.data.repositoryImpl

import android.util.Log
import com.project.veganlife.login.data.datasource.KakaoLoginDataSourceImpl
import com.project.veganlife.login.data.datasource.NaverLoginDataSourceImpl
import com.project.veganlife.login.data.model.LoginResponse
import com.project.veganlife.login.data.datasource.LoginRemoteDataSourceImpl
import com.project.veganlife.login.data.model.LoginRequest
import com.project.veganlife.login.domain.repository.LoginRepository
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val kakaoLoginDataSource: KakaoLoginDataSourceImpl,
    private val naverLoginDataSource: NaverLoginDataSourceImpl,
    private val loginRemoteDataSource: LoginRemoteDataSourceImpl,
) : LoginRepository {
    override suspend fun login(provider: String): String {
        var accessToken = ""
        if (provider == "kakao") {
            accessToken = kakaoLoginDataSource.login()

        } else if (provider == "naver") {
            accessToken = naverLoginDataSource.login()
        }
        return accessToken
    }

    override suspend fun logout(provider: String): String {
        Log.d("logout repository",provider)
        return when (provider) {
            "kakao" -> {
                kakaoLoginDataSource.logout()
            }

            else -> {
                naverLoginDataSource.logout()
            }
        }
    }

    override suspend fun loginApi(
        provider: String,
        accessToken: LoginRequest?,
    ): LoginResponse? {
        return accessToken?.let { loginRemoteDataSource.loginApi(provider, it) }
    }
}