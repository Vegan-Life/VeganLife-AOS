package com.project.veganlife.signup.data.datasource

import com.google.gson.GsonBuilder
import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.data.model.ConflictResponse
import com.project.veganlife.data.model.ProfileResponse
import com.project.veganlife.signup.data.remote.SignupApi
import okhttp3.RequestBody
import java.lang.Exception
import javax.inject.Inject

class SignupAddInfoRemoteDataSourceImpl @Inject constructor(
    private val signupApi: SignupApi,
)  {
    suspend fun signupAddInfo(
        signupRequestDTO: RequestBody
    ): ApiResult<ProfileResponse> {
        val gson = GsonBuilder().create()

        return try {
            val profileInfoGetResponse = signupApi.signupAddInfo(
                signupRequestDTO
            )

            if (profileInfoGetResponse.isSuccessful) {
                val responseBody = profileInfoGetResponse.body()!!
                ApiResult.Success(responseBody)
            } else {
                val errorBodyString = profileInfoGetResponse.errorBody()?.string()
                val conflictResponse =
                    gson.fromJson(errorBodyString, ConflictResponse::class.java)
                ApiResult.Error(conflictResponse.errorCode, conflictResponse.description)
            }

        } catch (e: Exception) {
            ApiResult.Exception(e)
        }
    }
}