package com.project.veganlife.lifecheck.data.datasource

import com.google.gson.GsonBuilder
import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.data.model.ConflictResponse
import com.project.veganlife.lifecheck.data.model.LifeCheckMealDataRequest
import com.project.veganlife.lifecheck.data.remote.LifeCheckApi
import javax.inject.Inject

class LifeCheckModifyMealDataSourceImpl @Inject constructor(
    private val lifeCheckApi: LifeCheckApi,
) {
    suspend fun modifyMealData(
        mealId: Long,
        updatedData: LifeCheckMealDataRequest
    ): ApiResult<Unit> {
        val gson = GsonBuilder().create()
        return try {
            val response = lifeCheckApi.modifyMealData(mealId, updatedData)
            if (response?.isSuccessful == true) {
                ApiResult.Success(Unit)
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