package com.project.veganlife.data.remote

import com.project.veganlife.data.model.DailyIntakeResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface DailyIntakeGetApi {
    @GET("members/nutrients/day")
    suspend fun getDailyIntake(
        @Header("Authorization") accessToken: String?,
        @Query("date") data: String,
    ): Response<DailyIntakeResponse>?
}
