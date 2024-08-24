package com.project.veganlife.domain.usecase

import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.domain.repository.RecipeLikeRepository
import javax.inject.Inject

class RecipeLikeUsecase @Inject constructor(
    private val recipeLikeRepository: RecipeLikeRepository
) {
    suspend fun recipeLike(id: Long): ApiResult<Int> {
        return recipeLikeRepository.setRecipeLike(id)
    }

    suspend fun recipeLikeCancel(id: Long): ApiResult<Int> {
        return recipeLikeRepository.setRecipeLikeCancel(id)
    }
}