package com.project.veganlife.community.domain.repository

import androidx.paging.PagingData
import com.project.veganlife.community.data.model.Feed
import kotlinx.coroutines.flow.Flow

interface CommunityRepository {
    suspend fun getFeeds(): Flow<PagingData<Feed>>
    suspend fun getFeedsByTag(tag: String): Flow<PagingData<Feed>>

    suspend fun searchFeedsByKeyword(keyword: String): Flow<PagingData<Feed>>

    suspend fun saveRecentSearches(recentSearches: List<String>)

    fun getRecentSearches(): Flow<List<String>>
}
