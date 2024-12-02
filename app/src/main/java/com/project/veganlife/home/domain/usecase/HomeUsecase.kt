package com.project.veganlife.home.domain.usecase

import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.data.model.DailyIntakeResponse
import com.project.veganlife.data.model.RecommendedIntakeResponse
import com.project.veganlife.home.domain.repository.HomeRepository
import javax.inject.Inject

class HomeUsecase @Inject constructor(
    private val homeRepository: HomeRepository
) {
    suspend fun getDailyIntake(): ApiResult<DailyIntakeResponse>? {
        return homeRepository.getDailyIntake()
    }

    suspend fun getRecommendedIntake(): ApiResult<RecommendedIntakeResponse>? {
        return homeRepository.getRecommendedIntake()
    }
}
