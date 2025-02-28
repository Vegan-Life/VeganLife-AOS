package com.project.veganlife.lifecheck.data.datasource

import com.google.gson.GsonBuilder
import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.data.model.ConflictResponse
import com.project.veganlife.lifecheck.data.remote.LifeCheckApi
import javax.inject.Inject

class LifeCheckDeleteMealLogDataSourceImpl @Inject constructor(
    private val lifeCheckApi: LifeCheckApi,
) {
    suspend fun deleteMealLog(
        mealLogId: Long,
    ): ApiResult<Unit> {
        val gson = GsonBuilder().create()
        return try {
            val response = lifeCheckApi.deleteMealLog(mealLogId)
            if (response.isSuccessful) {
                ApiResult.Success(Unit)
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