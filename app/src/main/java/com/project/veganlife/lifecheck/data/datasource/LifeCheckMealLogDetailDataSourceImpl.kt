package com.project.veganlife.lifecheck.data.datasource

import com.google.gson.GsonBuilder
import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.data.model.ConflictResponse
import com.project.veganlife.lifecheck.data.model.LifeCheckMealLogDetailResponse
import com.project.veganlife.lifecheck.data.remote.LifeCheckApi
import javax.inject.Inject

class LifeCheckMealLogDetailDataSourceImpl @Inject constructor(
    private val lifeCheckApi: LifeCheckApi
) {
    suspend fun getMealLogDetail(mealLogId: Long): ApiResult<LifeCheckMealLogDetailResponse> {
        val gson = GsonBuilder().create()
        return try {
            val response = lifeCheckApi.getMealLogDetail(mealLogId)
            if (response.isSuccessful) {
                ApiResult.Success(response.body()!!)
            } else {
                val errorBodyString = response.errorBody()?.string()
                val conflictResponse =
                    gson.fromJson(errorBodyString, ConflictResponse::class.java)
                ApiResult.Error(conflictResponse.errorCode, conflictResponse.description)
            }
        } catch (e: Exception) {
            ApiResult.Exception(e)
        }
    }
}