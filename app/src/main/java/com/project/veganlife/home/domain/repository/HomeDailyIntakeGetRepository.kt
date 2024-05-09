package com.project.veganlife.home.domain.repository

import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.data.model.DailyIntakeResponse

interface HomeDailyIntakeGetRepository {
    suspend fun getRecommendedIntake(
    ): ApiResult<DailyIntakeResponse>?
}