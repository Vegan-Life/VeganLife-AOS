package com.project.veganlife.home.domain.repository

import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.data.model.DailyIntakeResponse
import com.project.veganlife.data.model.RecommendedIntakeResponse

interface HomeRepository {
    suspend fun getDailyIntake(
    ): ApiResult<DailyIntakeResponse>?

    suspend fun getRecommendedIntake(
    ): ApiResult<RecommendedIntakeResponse>?
}