package com.project.veganlife.community.data.remote

import android.content.SharedPreferences
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.project.veganlife.community.data.model.Feed
import retrofit2.HttpException
import javax.inject.Inject

class CommunityFeedPagingSource @Inject constructor(
    private val api: CommunityApi,
    private val sharedPreferences: SharedPreferences
) : PagingSource<Int, Feed>() {
    override fun getRefreshKey(state: PagingState<Int, Feed>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Feed> {
        val page = params.key ?: 0
        return try {
            val accessToken = sharedPreferences.getString("ApiAccessToken", null)
            val response = api.getAllFeeds(
                accessToken,
                page,
                params.loadSize,
                "createdAt,DESC"
            )
            val responseData = response.body()
            LoadResult.Page(
                data = responseData?.content ?: emptyList(),
                prevKey = if (page == 0) null else page - 1,
                nextKey = if (responseData?.last == true) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }
}