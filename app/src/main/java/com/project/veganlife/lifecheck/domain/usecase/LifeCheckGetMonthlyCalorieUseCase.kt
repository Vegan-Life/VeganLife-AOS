package com.project.veganlife.lifecheck.domain.usecase

import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.lifecheck.data.model.LifeCheckWeeklyCalorieResponse
import com.project.veganlife.lifecheck.domain.repository.LifeCheckMonthlyCalorieRepository
import javax.inject.Inject

class LifeCheckGetMonthlyCalorieUseCase @Inject constructor(
    private val repository: LifeCheckMonthlyCalorieRepository
) {
    suspend operator fun invoke(startDate: String): ApiResult<LifeCheckWeeklyCalorieResponse> {
        return repository.getMonthlyCalorie(startDate)
    }
}