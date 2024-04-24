package com.project.veganlife.home.data.datasource

import android.content.SharedPreferences
import android.util.Log
import com.google.gson.GsonBuilder
import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.data.model.ConflictResponse
import com.project.veganlife.data.model.RecommendedIntakeResponse
import com.project.veganlife.data.remote.RecommendedIntakeGetApi
import java.lang.Exception
import javax.inject.Inject


class HomeRecommendedIntakeGetDataSourceImpl @Inject constructor(
    private val recommendedIntakeGetApi: RecommendedIntakeGetApi,
    private val accessToken: SharedPreferences,
) : HomeRecommendedIntakeGetDataSource {
    override suspend fun getRecommendedIntake(): ApiResult<RecommendedIntakeResponse>? {
        val gson = GsonBuilder().create()

        return try {
            val recommendedIntakeGetResponse = recommendedIntakeGetApi.getRecommendedIntake(
                accessToken.getString("ApiAccessToken", null),
            )
            if (recommendedIntakeGetResponse?.isSuccessful == true) {
                val responseBodyString = recommendedIntakeGetResponse.body()!!

                ApiResult.Success(responseBodyString)
            } else {
                val errorBodyString = recommendedIntakeGetResponse?.errorBody()?.string()
                val conflictResponse =
                    gson.fromJson(errorBodyString, ConflictResponse::class.java)
                ApiResult.Error(conflictResponse.errorCode, conflictResponse.description)
            }

        } catch (e: Exception) {
            ApiResult.Exception(e)
        }
    }
}
