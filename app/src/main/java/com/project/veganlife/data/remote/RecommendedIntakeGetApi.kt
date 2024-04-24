package com.project.veganlife.data.remote

import com.project.veganlife.data.model.RecommendedIntakeResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface RecommendedIntakeGetApi {
    @GET("members/nutrients")
    suspend fun getRecommendedIntake(
        @Header("Authorization") accessToken: String?,
    ): Response<RecommendedIntakeResponse>?
}