package com.project.veganlife.data.remote

import com.project.veganlife.data.model.ProfileResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface ProfileInfoGetApi {
    @GET("members/profile")
    suspend fun getInformation(
        @Header("Authorization") accessToken: String?,
    ): Response<ProfileResponse>
}