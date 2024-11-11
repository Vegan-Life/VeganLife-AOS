package com.project.veganlife.recipe.domain.usecase

import androidx.paging.PagingData
import com.project.veganlife.recipe.data.model.RecipeFeedContent
import com.project.veganlife.recipe.domain.repository.RecipeRepository
import kotlinx.coroutines.flow.Flow
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
}