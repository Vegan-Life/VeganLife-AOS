package com.project.veganlife.signup.data.remote

import android.content.SharedPreferences
import com.project.veganlife.signup.data.model.SignupRequest
import com.project.veganlife.signup.data.model.SignupResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface SignupApi {
    @POST("members")
    suspend fun getInformation(
        @Header("Authorization") accessToken: String?,
        @Body signupRequest: SignupRequest,
    ): Response<SignupResponse>
}