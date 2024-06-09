package com.project.veganlife.community.data.repositoryimpl

import android.content.SharedPreferences
import com.project.veganlife.community.data.model.Feeds
import com.project.veganlife.community.data.remote.CommunityRemoteDataSource
import com.project.veganlife.community.domain.repository.CommunityRepository
import com.project.veganlife.data.model.ApiResult
import javax.inject.Inject

class CommunityRepositoryImpl @Inject constructor(
    private val communityRemoteDataSource: CommunityRemoteDataSource,
    private val sharedPreferences: SharedPreferences
) : CommunityRepository {

    val accessToken = sharedPreferences.getString("ApiAccessToken", null) ?: ""

    override suspend fun getFeeds(): ApiResult<Feeds> {
        return communityRemoteDataSource.getFeeds(accessToken)
    }

    override suspend fun getFeedsByTag(tag: String): ApiResult<Feeds> {
        return communityRemoteDataSource.searchFeedsByKeyword(accessToken, tag)
    }

    override suspend fun searchFeedsByKeyword(keyword: String): ApiResult<Feeds> {
        return communityRemoteDataSource.searchFeedsByKeyword(accessToken, keyword)
    }

}
