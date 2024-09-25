package com.project.veganlife.data.remote

import com.project.veganlife.data.model.ProfileResponse
import retrofit2.Response
import retrofit2.http.GET

interface ProfileInfoGetApi {
    @GET("members/profile")
    suspend fun getInformation(): Response<ProfileResponse>
}