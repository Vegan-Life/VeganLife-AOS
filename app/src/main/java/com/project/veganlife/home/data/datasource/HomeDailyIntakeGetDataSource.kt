package com.project.veganlife.home.data.datasource

import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.data.model.DailyIntakeResponse

interface HomeDailyIntakeGetDataSource {
    suspend fun getDailyIntake(
    ): ApiResult<DailyIntakeResponse>?
}