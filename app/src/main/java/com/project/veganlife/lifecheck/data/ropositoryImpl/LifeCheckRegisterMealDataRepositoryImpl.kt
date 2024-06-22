package com.project.veganlife.lifecheck.data.ropositoryImpl

import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.lifecheck.data.datasource.LifeCheckRegisterMealDataDataSource
import com.project.veganlife.lifecheck.data.model.LifeCheckMealDataRequest
import com.project.veganlife.lifecheck.domain.repository.LifeCheckRegisterMealDataRepository
import javax.inject.Inject

class LifeCheckRegisterMealDataRepositoryImpl @Inject constructor(
    private val datasource: LifeCheckRegisterMealDataDataSource
) : LifeCheckRegisterMealDataRepository {
    override suspend fun registerMealData(mealData: LifeCheckMealDataRequest): ApiResult<LifeCheckMealDataRequest> {
        return datasource.registerMealData(mealData)
    }
}