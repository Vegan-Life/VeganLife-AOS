package com.project.veganlife.lifecheck.data.datasource

import com.google.gson.GsonBuilder
import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.data.model.ConflictResponse
import com.project.veganlife.lifecheck.data.remote.LifeCheckApi
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class LifeCheckRegisterMealLogDataSourceImpl @Inject constructor(
    private val mealLogApi: LifeCheckApi
) {
    suspend fun registerMealLog(
        mealLogRequest: RequestBody,
        images: List<MultipartBody.Part>?
    ): ApiResult<Unit> {
        val gson = GsonBuilder().create()
        return try {
            val response = mealLogApi.registerMealLog(mealLogRequest, images)
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