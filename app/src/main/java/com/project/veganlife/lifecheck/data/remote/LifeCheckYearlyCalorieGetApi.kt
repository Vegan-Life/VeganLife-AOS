package com.project.veganlife.lifecheck.data.remote

import com.project.veganlife.lifecheck.data.model.LifeCheckWeeklyCalorieResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface LifeCheckYearlyCalorieGetApi {
    @GET("members/nutrients/year")
    suspend fun getYearlyCalorie(
        @Header("Authorization") accessToken: String?,
        @Query("startDate") startDate: String
    ): Response<LifeCheckWeeklyCalorieResponse>?
}