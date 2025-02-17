package com.project.veganlife.lifecheck.data.datasource

import com.google.gson.GsonBuilder
import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.data.model.ConflictResponse
import com.project.veganlife.lifecheck.data.model.LifeCheckMealLogListResponse
import com.project.veganlife.lifecheck.data.remote.LifeCheckApi
import javax.inject.Inject

class LifeCheckMealLogListDataSourceImpl @Inject constructor(
    private val lifeCheckApi: LifeCheckApi
) {
    suspend fun getMealLogList(date: String): ApiResult<List<LifeCheckMealLogListResponse>> {
        val gson = GsonBuilder().create()
        return try {
            val response = lifeCheckApi.getMealLogList(date)
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