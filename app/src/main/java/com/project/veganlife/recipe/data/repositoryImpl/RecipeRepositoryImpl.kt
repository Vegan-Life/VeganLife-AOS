package com.project.veganlife.recipe.data.repositoryImpl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.recipe.data.datasource.RecipeAllFeedsPagingSource
import com.project.veganlife.recipe.data.datasource.RecipeDeleteDataSourceImpl
import com.project.veganlife.recipe.data.datasource.RecipeDetailContentDataSourceImpl
import com.project.veganlife.recipe.data.datasource.RecipeFeedByTypePagingSource
import com.project.veganlife.recipe.data.datasource.RecipeLikeDataSourceImpl
import com.project.veganlife.recipe.data.datasource.RecipeModifyDataSourceImpl
import com.project.veganlife.recipe.data.model.RecipeDetailContent
import com.project.veganlife.recipe.data.model.RecipeFeedContent
import com.project.veganlife.recipe.data.remote.RecipeApi
import com.project.veganlife.recipe.domain.repository.RecipeRepository
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class RecipeRepositoryImpl @Inject constructor(
    private val recipeApi: RecipeApi,
    private val recipeDetailContentDataSourceImpl: RecipeDetailContentDataSourceImpl,
    private val recipeDeleteDataSourceImpl: RecipeDeleteDataSourceImpl,
    private val recipeModifyDataSourceImpl: RecipeModifyDataSourceImpl,
    private val recipeLikeDataSourceImpl: RecipeLikeDataSourceImpl,
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

    override suspend fun getRecipeDetail(id: Long): ApiResult<RecipeDetailContent> {
        return recipeDetailContentDataSourceImpl.getRecipeDetailContent(id)
    }

    override suspend fun deleteRecipe(id: Long): ApiResult<Any> {
        return recipeDeleteDataSourceImpl.deleteRecipe(id)
    }

    override suspend fun modifyRecipe(
        id: Long,
        recipeRequestDTO: RequestBody,
        recipePhotoMultipart: MultipartBody.Part
    ): ApiResult<Any> {
        return recipeModifyDataSourceImpl.modifyRecipe(id,recipeRequestDTO,recipePhotoMultipart)
    }

    override suspend fun likeRecipe(id: Long): ApiResult<Any> {
        return recipeLikeDataSourceImpl.likeRecipe(id)
    }

    override suspend fun likeCancelRecipe(id: Long): ApiResult<Any> {
        return recipeLikeDataSourceImpl.likeCancelRecipe(id)
    }
}