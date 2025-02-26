package com.project.veganlife.lifecheck.data.remote

import com.project.veganlife.data.model.PagingResponse
import com.project.veganlife.lifecheck.data.model.LifeCheckMealData
import com.project.veganlife.lifecheck.data.model.LifeCheckMealDataDetail
import com.project.veganlife.lifecheck.data.model.LifeCheckMealDataRequest
import com.project.veganlife.lifecheck.data.model.LifeCheckMealLogDetailResponse
import com.project.veganlife.lifecheck.data.model.LifeCheckMealLogListResponse
import com.project.veganlife.lifecheck.data.model.LifeCheckWeeklyCalorieResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
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

    @DELETE("meal-data/{mealId}")
    suspend fun deleteMealData(@Path("mealId") mealId: Long): Response<Unit>?

    @Multipart
    @POST("meal-log")
    suspend fun registerMealLog(
        @Part("request") request: RequestBody,
        @Part images: List<MultipartBody.Part>?
    ): Response<Unit>

    @GET("meal-log")
    suspend fun getMealLogList(
        @Query("date") date: String
    ): Response<List<LifeCheckMealLogListResponse>>

    @GET("meal-log/{mealLogId}")
    suspend fun getMealLogDetail(
        @Path("mealLogId") mealLogId: Long
    ): Response<LifeCheckMealLogDetailResponse>

    @Multipart
    @PUT("meal-log/{mealLogId}")
    suspend fun modifyMealLog(
        @Path("mealLogId") mealLogId: Long,
        @Part("request") request: RequestBody,
        @Part images: List<MultipartBody.Part>?
    ): Response<Unit>

    @DELETE("meal-log/{mealLogId}")
    suspend fun deleteMealLog(@Path("mealLogId") mealLogId: Long): Response<Unit>
}