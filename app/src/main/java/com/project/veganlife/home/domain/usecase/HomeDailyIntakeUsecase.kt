package com.project.veganlife.home.domain.usecase

import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.data.model.DailyIntakeResponse
import com.project.veganlife.home.domain.repository.HomeDailyIntakeGetRepository
import javax.inject.Inject

class HomeDailyIntakeUsecase @Inject constructor(
    val homeDailyIntakeGetRepository: HomeDailyIntakeGetRepository,
) {
    suspend operator fun invoke(): ApiResult<DailyIntakeResponse>? {
        return homeDailyIntakeGetRepository.getDailyIntake()
    }
}
