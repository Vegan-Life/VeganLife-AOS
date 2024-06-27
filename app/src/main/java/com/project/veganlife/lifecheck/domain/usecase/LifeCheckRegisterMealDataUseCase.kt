package com.project.veganlife.lifecheck.domain.usecase

import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.lifecheck.data.model.LifeCheckMealDataRequest
import com.project.veganlife.lifecheck.domain.repository.LifeCheckRegisterMealDataRepository
import javax.inject.Inject

class LifeCheckRegisterMealDataUseCase @Inject constructor(
    private val repository: LifeCheckRegisterMealDataRepository
) {
    suspend operator fun invoke(data: LifeCheckMealDataRequest): ApiResult<LifeCheckMealDataRequest?> {
        return repository.registerMealData(data)
    }
}