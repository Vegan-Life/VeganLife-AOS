package com.project.veganlife.home.domain.usecase

import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.data.model.DailyIntakeResponse
import com.project.veganlife.home.data.datasource.HomeDailyIntakeGetDataSource
import javax.inject.Inject

class HomeDailyIntakeUsecase @Inject constructor(
    val homeDailyIntakeGetDataSource: HomeDailyIntakeGetDataSource,
) {
    suspend operator fun invoke(): ApiResult<DailyIntakeResponse>? {
        return homeDailyIntakeGetDataSource.getDailyIntake()
    }
}
