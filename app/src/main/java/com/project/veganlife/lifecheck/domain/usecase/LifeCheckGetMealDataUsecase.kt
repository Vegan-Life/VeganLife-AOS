package com.project.veganlife.lifecheck.domain.usecase

import androidx.paging.PagingData
import com.project.veganlife.lifecheck.data.model.LifeCheckMealData
import com.project.veganlife.lifecheck.domain.repository.LifeCheckMealDataRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LifeCheckGetMealDataUsecase @Inject constructor(
    private val repository: LifeCheckMealDataRepository
) {
    suspend operator fun invoke(
        keyword: String,
        ownerType: String
    ): Flow<PagingData<LifeCheckMealData>> {
        return repository.getMealDataStream(keyword, ownerType)
    }
}