package com.project.veganlife.lifecheck.domain.repository

import androidx.paging.PagingData
import com.project.veganlife.lifecheck.data.model.LifeCheckMealData
import kotlinx.coroutines.flow.Flow

interface LifeCheckMealDataRepository {
    fun getMealDataStream(keyword: String, ownerType: String): Flow<PagingData<LifeCheckMealData>>
}