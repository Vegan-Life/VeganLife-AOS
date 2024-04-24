package com.project.veganlife.home.domain.usecase

import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.data.model.RecommendedIntakeResponse
import com.project.veganlife.home.data.datasource.HomeRecommendedIntakeGetDataSource
import javax.inject.Inject

class HomeRecommededIntakeUsecase @Inject constructor(
    val homeRecommendedIntakeGetDataSource: HomeRecommendedIntakeGetDataSource,
) {
    suspend operator fun invoke(): ApiResult<RecommendedIntakeResponse>? {
        return homeRecommendedIntakeGetDataSource.getRecommendedIntake()
    }
}
