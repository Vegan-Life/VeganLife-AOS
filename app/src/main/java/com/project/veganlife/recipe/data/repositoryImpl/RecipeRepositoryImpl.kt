package com.project.veganlife.recipe.data.repositoryImpl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.project.veganlife.recipe.data.datasource.RecipeAllFeedsPagingSource
import com.project.veganlife.recipe.data.datasource.RecipeFeedByTypePagingSource
import com.project.veganlife.recipe.data.model.RecipeFeedContent
import com.project.veganlife.recipe.data.remote.RecipeApi
import com.project.veganlife.recipe.domain.repository.RecipeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RecipeRepositoryImpl @Inject constructor(
    private val recipeApi: RecipeApi,
) : RecipeRepository {
    override suspend fun getAllRecipeFeeds(): Flow<PagingData<RecipeFeedContent>> {
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = {
                RecipeAllFeedsPagingSource(
                    recipeApi
                )
            }
        ).flow
    }

    override suspend fun getRecipeFeedByType(type: String): Flow<PagingData<RecipeFeedContent>> {
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = {
                RecipeFeedByTypePagingSource(
                    type,
                    recipeApi
                )
            }
        ).flow
    }
}