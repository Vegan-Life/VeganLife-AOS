package com.project.veganlife.signup.data.datasource

import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.project.veganlife.data.model.ConflictResponse
import com.project.veganlife.signup.data.model.SignupRequest
import com.project.veganlife.signup.data.remote.SignupApi
import org.json.JSONObject
import java.lang.Exception
import javax.inject.Inject

class SignupAddInfoDataSourceImpl @Inject constructor(
    private val signupApi: SignupApi,
    private val accessToken: SharedPreferences,
) : SignupAddInfoDataSource {
    override suspend fun setSignup(signupRequest: SignupRequest): String? {
        try {
            val signupGetResponse = signupApi.getInformation(
                accessToken.getString("ApiAccessToken",null),
                signupRequest,
            )
            return if (signupGetResponse.code() == 200) {
                Log.d("SignupGetSuccess", signupGetResponse.code().toString())
                signupGetResponse.body()?.let { Log.d("SignupGetSuccess body", it.nickname) }
                signupGetResponse.body()?.nickname
            } else {
                val errorBody = signupGetResponse.errorBody()?.string()
                val conflictResponse = Gson().fromJson(errorBody, ConflictResponse::class.java)
                conflictResponse.description
            }
        } catch (e: Exception) {
            Log.e("SignupGetException", e.toString())
            return null
        }
    }
}