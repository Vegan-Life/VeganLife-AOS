package com.project.veganlife.lifecheck.data.remote

import com.project.veganlife.lifecheck.data.model.LifeCheckWeeklyCalorieResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface LifeCheckMonthlyCalorieGetApi {
    @GET("members/nutrients/month")
    suspend fun getMonthlyCalorie(
        @Query("startDate") startDate: String
    ): Response<LifeCheckWeeklyCalorieResponse>?
}