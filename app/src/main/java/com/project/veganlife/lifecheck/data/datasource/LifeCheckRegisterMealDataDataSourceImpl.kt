package com.project.veganlife.lifecheck.data.datasource

import android.content.SharedPreferences
import com.google.gson.GsonBuilder
import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.data.model.ConflictResponse
import com.project.veganlife.lifecheck.data.model.LifeCheckMealDataRequest
import com.project.veganlife.lifecheck.data.remote.LifeCheckMealDataPostApi
import javax.inject.Inject

class LifeCheckRegisterMealDataDataSourceImpl @Inject constructor(
    private val mealDataPostApi: LifeCheckMealDataPostApi,
    private val token: SharedPreferences
) : LifeCheckRegisterMealDataDataSource {
    override suspend fun registerMealData(mealData: LifeCheckMealDataRequest): ApiResult<LifeCheckMealDataRequest> {
        val gson = GsonBuilder().create()
        return try {
            val response =
                mealDataPostApi.registerMealData(token.getString("ApiAccessToken", null), mealData)
            if (response?.isSuccessful == true) {
                ApiResult.Success(response.body()!!)
            } else {
                val errorBodyString = response?.errorBody()?.string()
                val conflictResponse = gson.fromJson(errorBodyString, ConflictResponse::class.java)
                ApiResult.Error(conflictResponse.errorCode, conflictResponse.description)
            }
        } catch (e: Exception) {
            ApiResult.Exception(e)
        }
    }
}