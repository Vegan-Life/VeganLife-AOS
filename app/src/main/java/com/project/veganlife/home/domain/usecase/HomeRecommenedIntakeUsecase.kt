package com.project.veganlife.home.domain.usecase

import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.data.model.RecommendedIntakeResponse
import com.project.veganlife.home.domain.repository.HomeRecommendedIntakeGetRepository
import javax.inject.Inject

class HomeRecommenedIntakeUsecase @Inject constructor(
    val homeRecommendedIntakeGetRepository: HomeRecommendedIntakeGetRepository
) {
    suspend operator fun invoke(): ApiResult<RecommendedIntakeResponse>? {
        return homeRecommendedIntakeGetRepository.getRecommendedIntake()
    }
}
