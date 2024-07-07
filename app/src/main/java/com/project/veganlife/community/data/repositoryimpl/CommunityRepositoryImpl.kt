package com.project.veganlife.community.data.repositoryimpl

import android.content.SharedPreferences
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.project.veganlife.community.data.local.RecentSearchDataStoreManager
import com.project.veganlife.community.data.model.Feed
import com.project.veganlife.community.data.remote.CommunityApi
import com.project.veganlife.community.data.remote.CommunityFeedPagingSource
import com.project.veganlife.community.data.remote.KeywordFilteredFeedPagingSource
import com.project.veganlife.community.domain.repository.CommunityRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CommunityRepositoryImpl @Inject constructor(
    private val communityApi: CommunityApi,
    private val sharedPreferences: SharedPreferences,
    private val recentSearchDataStoreManager: RecentSearchDataStoreManager
) : CommunityRepository {
    override suspend fun getFeeds(): Flow<PagingData<Feed>> {
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = {
                CommunityFeedPagingSource(
                    communityApi,
                    sharedPreferences
                )
            }
        ).flow
    }

    override suspend fun getFeedsByTag(tag: String): Flow<PagingData<Feed>> {
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = {
                KeywordFilteredFeedPagingSource(
                    tag,
                    communityApi,
                    sharedPreferences
                )
            }
        ).flow
    }

    override suspend fun searchFeedsByKeyword(keyword: String): Flow<PagingData<Feed>> {
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = {
                KeywordFilteredFeedPagingSource(
                    keyword,
                    communityApi,
                    sharedPreferences
                )
            }
        ).flow
    }

    override suspend fun saveRecentSearches(recentSearches: List<String>) {
        recentSearchDataStoreManager.saveRecentSearch(recentSearches)
    }

    override fun getRecentSearches(): Flow<List<String>> {
        return recentSearchDataStoreManager.recentSearch
    }

}
