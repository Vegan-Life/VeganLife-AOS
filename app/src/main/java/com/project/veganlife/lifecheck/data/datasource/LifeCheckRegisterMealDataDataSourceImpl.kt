package com.project.veganlife.lifecheck.data.datasource

import com.google.gson.GsonBuilder
import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.data.model.ConflictResponse
import com.project.veganlife.lifecheck.data.model.LifeCheckMealDataRequest
import com.project.veganlife.lifecheck.data.remote.LifeCheckApi
import javax.inject.Inject

class LifeCheckRegisterMealDataDataSourceImpl @Inject constructor(
    private val mealDataPostApi: LifeCheckApi,
) {
    suspend fun registerMealData(mealData: LifeCheckMealDataRequest): ApiResult<Unit> {
        val gson = GsonBuilder().create()
        return try {
            val response =
                mealDataPostApi.registerMealData(mealData)
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