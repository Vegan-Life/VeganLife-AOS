package com.project.veganlife.data.remote

import com.project.veganlife.data.model.RecommendedIntakeResponse
import retrofit2.Response
import retrofit2.http.GET

interface RecommendedIntakeGetApi {
    @GET("members/nutrients")
    suspend fun getRecommendedIntake(
    ): Response<RecommendedIntakeResponse>?
}