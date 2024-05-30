package com.project.veganlife.lifecheck.domain.usecase

import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.lifecheck.data.model.LifeCheckWeeklyCalorieResponse
import com.project.veganlife.lifecheck.domain.repository.LifeCheckWeeklyCalorieRepository
import javax.inject.Inject

class LifeCheckGetWeeklyCalorieUseCase @Inject constructor(
    private val repository: LifeCheckWeeklyCalorieRepository
) {
    suspend operator fun invoke(
        startDate: String,
        endDate: String
    ): ApiResult<LifeCheckWeeklyCalorieResponse> {
        return repository.getWeeklyCalorie(startDate, endDate)
    }
}