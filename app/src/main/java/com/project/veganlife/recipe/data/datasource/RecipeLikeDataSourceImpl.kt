package com.project.veganlife.recipe.data.datasource

import com.google.gson.GsonBuilder
import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.data.model.ConflictResponse
import com.project.veganlife.recipe.data.remote.RecipeApi
import javax.inject.Inject
import kotlin.Exception

class RecipeLikeDataSourceImpl @Inject constructor(
    private val recipeApi: RecipeApi,
) {
    val gson = GsonBuilder().create()

    suspend fun likeRecipe(id: Long): ApiResult<Any> {
        return try {
            val response = recipeApi.likeRecipe(id)

            if (response.isSuccessful == true) {
                ApiResult.Success(response.code())
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

    suspend fun likeCancelRecipe(id: Long): ApiResult<Any> {
        return try {
            val response = recipeApi.likeCancelRecipe(id)

            if (response.isSuccessful == true) {
                ApiResult.Success(response.code())
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