package com.project.veganlife.community.data.repositoryimpl

import android.content.SharedPreferences
import com.project.veganlife.community.data.model.Feed
import com.project.veganlife.community.data.model.Feeds
import com.project.veganlife.community.data.remote.CommunityRemoteDataSource
import com.project.veganlife.community.domain.repository.CommunityRepository
import com.project.veganlife.data.model.ApiResult
import javax.inject.Inject

class CommunityRepositoryImpl @Inject constructor(
    private val communityRemoteDataSource: CommunityRemoteDataSource,
    private val sharedPreferences: SharedPreferences
) : CommunityRepository {
    override suspend fun getFeeds(): ApiResult<Feeds> {
        val accessToken = sharedPreferences.getString("ApiAccessToken", null)?:""
        return communityRemoteDataSource.getFeeds(accessToken)
    }

}
