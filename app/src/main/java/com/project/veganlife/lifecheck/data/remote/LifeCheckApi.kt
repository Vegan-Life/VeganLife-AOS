package com.project.veganlife.lifecheck.data.remote

import com.project.veganlife.data.model.PagingResponse
import com.project.veganlife.lifecheck.data.model.LifeCheckMealData
import com.project.veganlife.lifecheck.data.model.LifeCheckMealDataDetail
import com.project.veganlife.lifecheck.data.model.LifeCheckMealDataRequest
import com.project.veganlife.lifecheck.data.model.LifeCheckWeeklyCalorieResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface LifeCheckApi {
    @GET("meal-data")
    suspend fun getMealData(
        @Query("keyword") keyword: String,
        @Query("ownerType") ownerType: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Response<PagingResponse<LifeCheckMealData>>

    @POST("meal-data")
    suspend fun registerMealData(
        @Body lifeCheckMealDataRequest: LifeCheckMealDataRequest
    ): Response<Unit>

    @GET("members/nutrients/month")
    suspend fun getMonthlyCalorie(
        @Query("startDate") startDate: String
    ): Response<LifeCheckWeeklyCalorieResponse>?

    @GET("members/nutrients/week")
    suspend fun getWeeklyCalorie(
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String
    ): Response<LifeCheckWeeklyCalorieResponse>?

    @GET("members/nutrients/year")
    suspend fun getYearlyCalorie(
        @Query("startDate") startDate: String
    ): Response<LifeCheckWeeklyCalorieResponse>?

    @GET("meal-data/{id}")
    suspend fun getMealDataById(
        @Path("id") id: Long
    ): Response<LifeCheckMealDataDetail>

    @PUT("meal-data/{mealId}")
    suspend fun modifyMealData(
        @Path("mealId") mealId: Long,
        @Body request: LifeCheckMealDataRequest
    ): Response<Unit>?
}