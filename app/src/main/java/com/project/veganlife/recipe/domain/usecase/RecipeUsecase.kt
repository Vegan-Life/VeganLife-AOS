package com.project.veganlife.recipe.domain.usecase

import androidx.paging.PagingData
import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.recipe.data.model.RecipeDetailContent
import com.project.veganlife.recipe.data.model.RecipeFeedContent
import com.project.veganlife.recipe.domain.repository.RecipeRepository
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class RecipeUsecase @Inject constructor(
    private val recipeRepository: RecipeRepository,
) {
    suspend fun getRecipeAllFeeds(): Flow<PagingData<RecipeFeedContent>> {
        return recipeRepository.getAllRecipeFeeds()
    }

    suspend fun getRecipeFeedByType(type: String): Flow<PagingData<RecipeFeedContent>> {
        return recipeRepository.getRecipeFeedByType(type)
    }

    suspend fun getRecipeDetailContent(id: Long): ApiResult<RecipeDetailContent> {
        return recipeRepository.getRecipeDetail(id)
    }

    suspend fun deleteRecipe(id: Long): ApiResult<Any> {
        return recipeRepository.deleteRecipe(id)
    }

    suspend fun modifyRecipe(
        id: Long,
        recipeRequetDTO: RequestBody,
        recipePhotoMultipart: List<MultipartBody.Part>
    ): ApiResult<Any> {
        return recipeRepository.modifyRecipe(id, recipeRequetDTO, recipePhotoMultipart)
    }

    suspend fun likeRecipe(id: Long): ApiResult<Any> {
        return recipeRepository.likeRecipe(id)
    }

    suspend fun likeCancelRecipe(id: Long): ApiResult<Any> {
        return recipeRepository.likeCancelRecipe(id)
    }

    suspend fun registerRecipe(recipeRequestDTO: RequestBody, recipePhotoMultipart: List<MultipartBody.Part>): ApiResult<Any>{
        return recipeRepository.registerRecipe(recipeRequestDTO, recipePhotoMultipart)
    }

    suspend fun getRecommendRecipe(): ApiResult<List<RecipeFeedContent>> {
        return recipeRepository.getRecommendRecipe()
    }

    suspend fun getSearchRecipe(keyword: String): Flow<PagingData<RecipeFeedContent>> {
        return recipeRepository.getSearchRecipe(keyword)
    }

    suspend fun saveResentSearchRecipe(recentSearches: List<String>) {
        recipeRepository.saveRecentSearches(recentSearches)
    }

    fun getResentSearchRecipe(): Flow<List<String>> {
        return recipeRepository.getRecentSearches()
    }
}