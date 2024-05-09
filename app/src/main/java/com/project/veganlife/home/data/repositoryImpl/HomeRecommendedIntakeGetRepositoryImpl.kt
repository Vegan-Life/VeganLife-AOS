package com.project.veganlife.home.data.repositoryImpl

import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.data.model.RecommendedIntakeResponse
import com.project.veganlife.home.data.datasource.HomeRecommendedIntakeGetDataSource
import com.project.veganlife.home.domain.repository.HomeRecommendedIntakeGetRepository
import javax.inject.Inject

class HomeRecommendedIntakeGetRepositoryImpl @Inject constructor(
    private val homeRecommendedIntakeGetDataSource: HomeRecommendedIntakeGetDataSource,
): HomeRecommendedIntakeGetRepository{
    override suspend fun getRecommendedIntake(): ApiResult<RecommendedIntakeResponse>? {
        return homeRecommendedIntakeGetDataSource.getRecommendedIntake()
    }
}