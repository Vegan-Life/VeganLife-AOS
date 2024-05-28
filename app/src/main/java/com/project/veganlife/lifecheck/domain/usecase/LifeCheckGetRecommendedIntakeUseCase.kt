package com.project.veganlife.lifecheck.domain.usecase

import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.data.model.RecommendedIntakeResponse
import com.project.veganlife.lifecheck.domain.repository.LifeCheckRecommendedIntakeRepository
import javax.inject.Inject

class LifeCheckGetRecommendedIntakeUseCase @Inject constructor(
    private val repository: LifeCheckRecommendedIntakeRepository,
) {
    suspend operator fun invoke(): ApiResult<RecommendedIntakeResponse> {
        return repository.getRecommendedIntake()
    }
}