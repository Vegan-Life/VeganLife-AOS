package com.project.veganlife.login.data.remote

import android.util.Log
import com.project.veganlife.login.data.model.LoginRequest
import com.project.veganlife.login.data.model.LoginResponse
import com.project.veganlife.login.data.model.remote.LoginRetrofitClient
import org.json.JSONObject
import java.lang.Exception
import javax.inject.Inject

class LoginRemoteDataSource @Inject constructor() {
    private val loginApiService = LoginRetrofitClient.loginApi

    suspend fun loginApi(
        provider: String,
        accessToken: LoginRequest,
    ): LoginResponse? {
        try {
            val loginGetResponse = loginApiService.getAccessToken(
                provider,
                accessToken,
            )

            if (loginGetResponse.code() != 200) {
                val stringToJson = JSONObject(loginGetResponse.errorBody()?.string()!!)
                Log.d("LoginGetFailure", loginGetResponse.code().toString())
                Log.d("LoginGetFailure", "$stringToJson")
                return null
            }

            Log.d("LoginGetSuccess", loginGetResponse.code().toString())
            return loginGetResponse.body()
        } catch (e: Exception) {
            Log.e("LoginGetException", e.toString())
            return null
        }
    }
}