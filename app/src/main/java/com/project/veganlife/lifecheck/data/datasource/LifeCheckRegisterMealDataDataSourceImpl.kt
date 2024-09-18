package com.project.veganlife.lifecheck.data.datasource

import com.google.gson.GsonBuilder
import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.data.model.ConflictResponse
import com.project.veganlife.lifecheck.data.model.LifeCheckMealDataRequest
import com.project.veganlife.lifecheck.data.remote.LifeCheckMealDataPostApi
import javax.inject.Inject

class LifeCheckRegisterMealDataDataSourceImpl @Inject constructor(
    private val mealDataPostApi: LifeCheckMealDataPostApi,
) : LifeCheckRegisterMealDataDataSource {
    override suspend fun registerMealData(mealData: LifeCheckMealDataRequest): ApiResult<LifeCheckMealDataRequest?> {
        val gson = GsonBuilder().create()
        return try {
            val response =
                mealDataPostApi.registerMealData(mealData)
            if (response.isSuccessful) {
                ApiResult.Success(null)
            } else {
                val errorBodyString = response.errorBody()?.string()
                if (errorBodyString != null) {
                    val conflictResponse =
                        gson.fromJson(errorBodyString, ConflictResponse::class.java)
                    ApiResult.Error(
                        conflictResponse?.errorCode ?: "UNKNOWN_ERROR",
                        conflictResponse?.description ?: "Unknown error occurred."
                    )
                } else {
                    ApiResult.Error("UNKNOWN_ERROR", "Unknown error occurred.")
                }
            }
        } catch (e: Exception) {
            ApiResult.Exception(e)
        }
    }
}