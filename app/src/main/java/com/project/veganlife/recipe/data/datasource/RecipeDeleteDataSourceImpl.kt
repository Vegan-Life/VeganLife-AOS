package com.project.veganlife.recipe.data.datasource

import com.google.gson.GsonBuilder
import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.data.model.ConflictResponse
import com.project.veganlife.recipe.data.remote.RecipeApi
import javax.inject.Inject

class RecipeDeleteDataSourceImpl @Inject constructor(
    private val recipeApi: RecipeApi
) {
    suspend fun deleteRecipe(id: Long): ApiResult<Any> {
        val gson = GsonBuilder().create()

        return try {
            val response = recipeApi.deleteRecipe(id)

            if (response.isSuccessful) {
                val responseBody = response.code().toString()
                ApiResult.Success(responseBody)
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