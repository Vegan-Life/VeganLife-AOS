package com.project.veganlife.lifecheck.data.remote

import com.project.veganlife.lifecheck.data.model.LifeCheckMealDataRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface LifeCheckMealDataPostApi {
    @POST("meal-data")
    suspend fun registerMealData(
        @Body lifeCheckMealDataRequest: LifeCheckMealDataRequest
    ): Response<LifeCheckMealDataRequest?>
}