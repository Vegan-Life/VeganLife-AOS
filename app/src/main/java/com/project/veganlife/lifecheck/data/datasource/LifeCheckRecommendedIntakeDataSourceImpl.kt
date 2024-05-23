package com.project.veganlife.lifecheck.data.datasource

import android.content.SharedPreferences
import com.google.gson.GsonBuilder
import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.data.model.ConflictResponse
import com.project.veganlife.data.model.RecommendedIntakeResponse
import com.project.veganlife.data.remote.RecommendedIntakeGetApi
import javax.inject.Inject

class LifeCheckRecommendedIntakeDataSourceImpl @Inject constructor(
    private val lifeCheckRecommendedIntakeApi: RecommendedIntakeGetApi,
    private val token: SharedPreferences,
) : LifeCheckRecommendedIntakeDataSource {
    override suspend fun getRecommendedIntake(): ApiResult<RecommendedIntakeResponse> {
        val gson = GsonBuilder().create()
        return try {
            val response = lifeCheckRecommendedIntakeApi.getRecommendedIntake(
                token.getString("ApiAccessToken", null)
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