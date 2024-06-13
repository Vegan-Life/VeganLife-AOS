package com.project.veganlife.lifecheck.domain.usecase

import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.lifecheck.data.model.LifeCheckWeeklyCalorieResponse
import com.project.veganlife.lifecheck.domain.repository.LifeCheckYearlyCalorieRepository
import javax.inject.Inject

class LifeCheckGetYearlyCalorieUseCase @Inject constructor(
    private val repository: LifeCheckYearlyCalorieRepository
) {
    suspend operator fun invoke(startDate: String): ApiResult<LifeCheckWeeklyCalorieResponse> {
        return repository.getYearlyCalorie(startDate)
    }
}