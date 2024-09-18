package com.project.veganlife.lifecheck.data.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.project.veganlife.lifecheck.data.model.LifeCheckMealData
import com.project.veganlife.lifecheck.data.remote.LifeCheckMealDataApi
import javax.inject.Inject

class LifeCheckMealDataPagingSource @Inject constructor(
    private val mealDataApi: LifeCheckMealDataApi,
    private val keyword: String,
    private val ownerType: String,
) : PagingSource<Int, LifeCheckMealData>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LifeCheckMealData> {
        return try {
            val page = params.key ?: 0
            val size = params.loadSize
            val response = mealDataApi.getMealData(keyword, ownerType, page, size)
            val responseData = response.body()!!
            val mealData = responseData.content

            LoadResult.Page(
                data = mealData,
                prevKey = if (page == 0) null else page - 1,
                nextKey = if (responseData.last) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, LifeCheckMealData>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}