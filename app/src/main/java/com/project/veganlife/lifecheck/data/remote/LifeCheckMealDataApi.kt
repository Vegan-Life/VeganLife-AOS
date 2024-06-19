package com.project.veganlife.lifecheck.data.remote

import com.project.veganlife.data.model.PagingResponse
import com.project.veganlife.lifecheck.data.model.LifeCheckMealData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface LifeCheckMealDataApi {
    @GET("meal-data")
    suspend fun getMealData(
        @Header("Authorization") token: String,
        @Query("keyword") keyword: String,
        @Query("ownerType") ownerType: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Response<PagingResponse<LifeCheckMealData>>
}