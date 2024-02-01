package com.project.veganlife.community.data.repositoryimpl

import com.project.veganlife.community.data.remote.CommunityRemoteDataSource
import com.project.veganlife.community.domain.repository.CommunityRepository
import javax.inject.Inject

class CommunityRepositoryImpl @Inject constructor(
    private val communityRemoteDataSource: CommunityRemoteDataSource,
) : CommunityRepository {
    override suspend fun getFeeds(accessToken: String) =
        communityRemoteDataSource.getFeeds(accessToken)
}
