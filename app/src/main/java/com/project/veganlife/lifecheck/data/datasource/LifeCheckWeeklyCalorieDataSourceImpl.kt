package com.project.veganlife.lifecheck.data.datasource

import com.google.gson.GsonBuilder
import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.data.model.ConflictResponse
import com.project.veganlife.lifecheck.data.model.LifeCheckWeeklyCalorieResponse
import com.project.veganlife.lifecheck.data.remote.LifeCheckWeeklyCalorieGetApi
import javax.inject.Inject

class LifeCheckWeeklyCalorieDataSourceImpl @Inject constructor(
    private val weeklyCalorieApi: LifeCheckWeeklyCalorieGetApi,
) : LifeCheckWeeklyCalorieDataSource {
    override suspend fun getWeeklyCalorie(
        startDate: String,
        endDate: String
    ): ApiResult<LifeCheckWeeklyCalorieResponse> {
        val gson = GsonBuilder().create()
        return try {
            val response = weeklyCalorieApi.getWeeklyCalorie(
                startDate,
                endDate
            )
            if (response?.isSuccessful == true) {
                ApiResult.Success(response.body()!!)
            } else {
                val errorBodyString = response?.errorBody()?.string()
                val conflictResponse =
                    gson.fromJson(errorBodyString, ConflictResponse::class.java)
                ApiResult.Error(conflictResponse.errorCode, conflictResponse.description)
            }
        } catch (e: Exception) {
            ApiResult.Exception(e)
        }
    }
}