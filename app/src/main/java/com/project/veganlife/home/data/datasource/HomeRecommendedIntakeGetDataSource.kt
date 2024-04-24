package com.project.veganlife.home.data.datasource

import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.data.model.RecommendedIntakeResponse

interface HomeRecommendedIntakeGetDataSource {
    suspend fun getRecommendedIntake(
    ): ApiResult<RecommendedIntakeResponse>?
}