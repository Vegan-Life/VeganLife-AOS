package com.project.veganlife.lifecheck.data.ropositoryImpl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.project.veganlife.lifecheck.data.datasource.LifeCheckMealDataPagingSource
import com.project.veganlife.lifecheck.data.model.LifeCheckMealData
import com.project.veganlife.lifecheck.data.remote.LifeCheckMealDataApi
import com.project.veganlife.lifecheck.domain.repository.LifeCheckMealDataRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LifeCheckMealDataRepositoryImpl @Inject constructor(
    private val mealDataApi: LifeCheckMealDataApi,
) : LifeCheckMealDataRepository {
    override fun getMealDataStream(
        keyword: String,
        ownerType: String
    ): Flow<PagingData<LifeCheckMealData>> {
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = {
                LifeCheckMealDataPagingSource(
                    mealDataApi,
                    keyword,
                    ownerType,
                )
            }
        ).flow
    }
}