package com.project.veganlife.data.datasource

import android.content.SharedPreferences
import com.google.gson.GsonBuilder
import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.data.model.ConflictResponse
import com.project.veganlife.data.remote.RecipeLikeApi
import com.project.veganlife.data.remote.RecipeLikeCancelApi
import javax.inject.Inject
import kotlin.Exception

class RecipeLikeDataSourceImpl @Inject constructor(
    private val recipeLikeApi: RecipeLikeApi,
    private val recipeLikeCancelApi: RecipeLikeCancelApi,
    private val accessToken: SharedPreferences,
) : RecipeLikeDataSource {
    val gson = GsonBuilder().create()

    override suspend fun setRecipeLike(id: Long): ApiResult<Int> {
        return try {
            val recipeLikeResponse = recipeLikeApi.setRecipeLike(
                accessToken.getString("ApiAccessToken", null),
                id
            )

            if (recipeLikeResponse.isSuccessful == true) {
                ApiResult.Success(recipeLikeResponse.code())
            } else {
                val errorBodyString = recipeLikeResponse.errorBody()?.string()
                val conflictResponse =
                    gson.fromJson(errorBodyString, ConflictResponse::class.java)
                ApiResult.Error(conflictResponse.errorCode, conflictResponse.description)
            }

        } catch (e: Exception) {
            ApiResult.Exception(e)
        }
    }

    override suspend fun setRecipeLikeCancel(id: Long): ApiResult<Int> {
        return try {
            val recipeLikeCancelResponse = recipeLikeCancelApi.setRecipeLikeCencel(
                accessToken.getString("ApiAccessToken", null),
                id
            )

            if (recipeLikeCancelResponse.isSuccessful == true) {
                ApiResult.Success(recipeLikeCancelResponse.code())
            } else {
                val errorBodyString = recipeLikeCancelResponse.errorBody()?.string()
                val conflictResponse =
                    gson.fromJson(errorBodyString, ConflictResponse::class.java)
                ApiResult.Error(conflictResponse.errorCode, conflictResponse.description)
            }

        } catch (e: Exception) {
            ApiResult.Exception(e)
        }
    }

}