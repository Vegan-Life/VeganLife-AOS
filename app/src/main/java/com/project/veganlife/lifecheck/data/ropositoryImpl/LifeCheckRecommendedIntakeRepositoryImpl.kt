package com.project.veganlife.lifecheck.data.ropositoryImpl

import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.data.model.RecommendedIntakeResponse
import com.project.veganlife.lifecheck.data.datasource.LifeCheckRecommendedIntakeDataSource
import com.project.veganlife.lifecheck.domain.repository.LifeCheckRecommendedIntakeRepository
import javax.inject.Inject

class LifeCheckRecommendedIntakeRepositoryImpl @Inject constructor(
    private val dataSource: LifeCheckRecommendedIntakeDataSource,
) : LifeCheckRecommendedIntakeRepository {
    override suspend fun getRecommendedIntake(): ApiResult<RecommendedIntakeResponse> {
        return dataSource.getRecommendedIntake()
    }
}