package com.project.veganlife.recipe.domain.repository

import androidx.paging.PagingData
import com.project.veganlife.recipe.data.model.RecipeFeedContent
import kotlinx.coroutines.flow.Flow

interface RecipeRepository {
    suspend fun getAllRecipeFeeds(): Flow<PagingData<RecipeFeedContent>>
    suspend fun getRecipeFeedByType(type: String): Flow<PagingData<RecipeFeedContent>>
}