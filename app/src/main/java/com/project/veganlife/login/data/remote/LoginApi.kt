package com.project.veganlife.login.data.model.remote

import com.project.veganlife.login.data.model.LoginRequest
import com.project.veganlife.login.data.model.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface LoginApi {
    @POST("members/oauth/{provider}/login")
    suspend fun getAccessToken(
        @Path("provider") provider: String,
        @Body accessToken: LoginRequest,
    ): Response<LoginResponse>
}