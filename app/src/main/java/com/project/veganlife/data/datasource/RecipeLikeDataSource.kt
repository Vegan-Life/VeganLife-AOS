package com.project.veganlife.data.datasource

import com.project.veganlife.data.model.ApiResult

interface RecipeLikeDataSource {
    suspend fun setRecipeLike(id: Long): ApiResult<Int>

    suspend fun setRecipeLikeCancel(id: Long): ApiResult<Int>
}