package com.project.veganlife.recipe.domain.repository

import androidx.paging.PagingData
import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.recipe.data.model.RecipeDetailContent
import com.project.veganlife.recipe.data.model.RecipeFeedContent
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface RecipeRepository {
    suspend fun getAllRecipeFeeds(): Flow<PagingData<RecipeFeedContent>>
    suspend fun getRecipeFeedByType(type: String): Flow<PagingData<RecipeFeedContent>>
    suspend fun getRecipeDetail(id: Long): ApiResult<RecipeDetailContent>
    suspend fun deleteRecipe(id: Long): ApiResult<Any>
    suspend fun modifyRecipe(
        id: Long,
        recipeRequestDTO: RequestBody,
        recipePhotoMultipart: MultipartBody.Part
        ): ApiResult<Any>
    suspend fun likeRecipe(id: Long): ApiResult<Any>
    suspend fun likeCancelRecipe(id: Long): ApiResult<Any>
}