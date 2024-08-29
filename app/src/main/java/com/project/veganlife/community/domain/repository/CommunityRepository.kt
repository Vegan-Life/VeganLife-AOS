package com.project.veganlife.community.domain.repository

import androidx.paging.PagingData
import com.project.veganlife.community.data.model.PostPreview
import com.project.veganlife.community.data.model.PopularTagsResponse
import com.project.veganlife.community.data.model.Post
import com.project.veganlife.data.model.ApiResult
import kotlinx.coroutines.flow.Flow

interface CommunityRepository {
    suspend fun getFeeds(): Flow<PagingData<PostPreview>>
    suspend fun getFeedsByTag(tag: String): Flow<PagingData<PostPreview>>

    suspend fun searchFeedsByKeyword(keyword: String): Flow<PagingData<PostPreview>>

    suspend fun saveRecentSearches(recentSearches: List<String>)

    fun getRecentSearches(): Flow<List<String>>

    suspend fun getPopularityTags(): ApiResult<PopularTagsResponse>
    suspend fun getPostData(postId: Int): ApiResult<Post>
}
