package com.project.veganlife.lifecheck.data.datasource

import android.content.SharedPreferences
import com.google.gson.GsonBuilder
import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.data.model.ConflictResponse
import com.project.veganlife.data.model.DailyIntakeResponse
import com.project.veganlife.data.remote.DailyIntakeGetApi
import javax.inject.Inject

class LifeCheckDailyIntakeDataSourceImpl @Inject constructor(
    private val lifeCheckDailyIntakeApi: DailyIntakeGetApi,
    private val token: SharedPreferences,
) : LifeCheckDailyIntakeDataSource {
    override suspend fun getDailyIntake(date: String): ApiResult<DailyIntakeResponse> {
        val gson = GsonBuilder().create()
        return try {
            val response = lifeCheckDailyIntakeApi.getDailyIntake(
                token.getString("ApiAccessToken", null),
                date
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