package com.project.veganlife.lifecheck.data.remote

import com.project.veganlife.lifecheck.data.model.LifeCheckWeeklyCalorieResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface LifeCheckWeeklyCalorieGetApi {
    @GET("members/nutrients/week")
    suspend fun getWeeklyCalorie(
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String
    ): Response<LifeCheckWeeklyCalorieResponse>?
}