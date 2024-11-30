package com.project.veganlife.recipe.data.datasource

import com.google.gson.GsonBuilder
import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.data.model.ConflictResponse
import com.project.veganlife.recipe.data.remote.RecipeApi
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.lang.Exception
import javax.inject.Inject

class RecipeModifyDataSourceImpl @Inject constructor(
    private val recipeApi: RecipeApi,
) {
    suspend fun modifyRecipe(
        id: Long,
        recipeRequestDTO: RequestBody,
        recipePhotoMultipart: MultipartBody.Part
    ): ApiResult<Any> {
        val gson = GsonBuilder().create()

        return try {
            val response = recipeApi.modifyRecipe(
                id,
                recipeRequestDTO,
                recipePhotoMultipart,
            )

            if (response.isSuccessful) {
                val responseBody = response.code()
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