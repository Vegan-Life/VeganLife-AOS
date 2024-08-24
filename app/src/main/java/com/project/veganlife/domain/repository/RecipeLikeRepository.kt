package com.project.veganlife.domain.repository

import com.project.veganlife.data.model.ApiResult

interface RecipeLikeRepository {
    suspend fun setRecipeLike(id: Long): ApiResult<Int>

    suspend fun setRecipeLikeCancel(id: Long): ApiResult<Int>
}