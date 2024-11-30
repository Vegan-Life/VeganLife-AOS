package com.project.veganlife.recipe.data.datasource

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.project.veganlife.recipe.data.model.RecipeFeedContent
import com.project.veganlife.recipe.data.remote.RecipeApi
import retrofit2.HttpException
import javax.inject.Inject

class RecipeAllFeedsPagingSource @Inject constructor(
    private val api: RecipeApi,
) : PagingSource<Int, RecipeFeedContent>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, RecipeFeedContent> {
        val page = params.key ?: 0 // 초기 페이지 키가 없으면 0으로 설정합니다.

        return try {
            val response =
                api.getAllRecipeFeeds(
                    page,
                    params.loadSize,
                    "createdAt,DESC"
                )
            val responseData = response.body()!!

            // 응답이 성공적이고 데이터가 존재할 때
            Log.d("Source", "로드 성공")
            // 가져온 데이터 목록
            val recipeFeedsList = responseData.content

            // 페이징된 결과를 반환합니다.
            LoadResult.Page(
                data = recipeFeedsList,
                prevKey = if (page == 0) null else page - 1, // 이전 페이지 키 설정
                nextKey = if (responseData.last) null else page + 1 // 다음 페이지 키 설정
            )

        } catch (e: Exception) {
            LoadResult.Error(e) // 예외 발생 시
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, RecipeFeedContent>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            // 현재 페이지를 기준으로 가장 가까운 페이지의 키를 반환합니다.
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}