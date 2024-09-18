package com.project.veganlife.home.data.datasource

import com.google.gson.GsonBuilder
import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.data.model.ConflictResponse
import com.project.veganlife.data.model.RecommendedIntakeResponse
import com.project.veganlife.data.remote.RecommendedIntakeGetApi
import java.lang.Exception
import javax.inject.Inject

class HomeRecommendedIntakeGetDataSourceImpl @Inject constructor(
    private val recommendedIntakeGetApi: RecommendedIntakeGetApi,
) : HomeRecommendedIntakeGetDataSource {
    override suspend fun getRecommendedIntake(): ApiResult<RecommendedIntakeResponse>? {
        val gson = GsonBuilder().create()

        return try {
            val response = recommendedIntakeGetApi.getRecommendedIntake()
            if (response?.isSuccessful == true) {
                val responseBodyString = response.body()!!

                ApiResult.Success(responseBodyString)
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