package com.project.veganlife.home.domain.repository

import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.data.model.RecommendedIntakeResponse

interface HomeRecommendedIntakeGetRepository {
    suspend fun getRecommendedIntake(
    ): ApiResult<RecommendedIntakeResponse>?
}