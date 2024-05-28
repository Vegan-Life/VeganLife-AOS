package com.project.veganlife.lifecheck.domain.usecase

import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.data.model.DailyIntakeResponse
import com.project.veganlife.lifecheck.domain.repository.LifeCheckDailyIntakeRepository
import javax.inject.Inject

class LifeCheckGetDailyIntakeUseCase @Inject constructor(
    private val repository: LifeCheckDailyIntakeRepository,
) {
    suspend operator fun invoke(date: String): ApiResult<DailyIntakeResponse> {
        return repository.getDailyIntake(date)
    }
}