package com.project.veganlife.lifecheck.data.datasource

import com.google.gson.GsonBuilder
import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.data.model.ConflictResponse
import com.project.veganlife.lifecheck.data.model.LifeCheckWeeklyCalorieResponse
import com.project.veganlife.lifecheck.data.remote.LifeCheckYearlyCalorieGetApi
import javax.inject.Inject

class LifeCheckYearlyCalorieDataSourceImpl @Inject constructor(
    private val yearlyCalorieApi: LifeCheckYearlyCalorieGetApi,
) : LifeCheckYearlyCalorieDataSource {
    override suspend fun getYearlyCalorie(startDate: String): ApiResult<LifeCheckWeeklyCalorieResponse> {
        val gson = GsonBuilder().create()
        return try {
            val response = yearlyCalorieApi.getYearlyCalorie(
                startDate
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