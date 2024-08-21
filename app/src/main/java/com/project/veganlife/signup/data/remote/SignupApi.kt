package com.project.veganlife.signup.data.remote

import com.project.veganlife.data.model.ProfileResponse
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PUT
import retrofit2.http.Part

interface SignupApi {
    @Multipart
    @PUT("members/profile")
    suspend fun signupAddInfo(
        @Header("Authorization") accessToken: String?,
        @Part("request") signupRequestDTO: RequestBody,
    ): Response<ProfileResponse>
}