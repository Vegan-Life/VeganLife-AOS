package com.project.veganlife.data.repositoryImpl

import com.project.veganlife.data.datasource.RecipeLikeDataSource
import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.domain.repository.RecipeLikeRepository
import javax.inject.Inject

class RecipeLikeRepositoryImpl @Inject constructor(
    private val recipeLikeDataSource: RecipeLikeDataSource,
): RecipeLikeRepository {
    override suspend fun setRecipeLike(id: Long): ApiResult<Int> {
        return recipeLikeDataSource.setRecipeLike(id)
    }

    override suspend fun setRecipeLikeCancel(id: Long): ApiResult<Int> {
        return recipeLikeDataSource.setRecipeLikeCancel(id)
    }
}