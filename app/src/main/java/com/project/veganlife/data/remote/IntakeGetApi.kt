package com.project.veganlife.data.remote

import com.project.veganlife.data.model.DailyIntakeResponse
import com.project.veganlife.data.model.RecommendedIntakeResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface IntakeGetApi {
    @GET("members/nutrients")
    suspend fun getRecommendedIntake(
    ): Response<RecommendedIntakeResponse>?

    @GET("members/nutrients/day")
    suspend fun getDailyIntake(
        @Query("date") data: String,
    ): Response<DailyIntakeResponse>?
}