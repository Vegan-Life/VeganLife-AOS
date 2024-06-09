package com.project.veganlife.community.domain.repository

import com.project.veganlife.community.data.model.Feeds
import com.project.veganlife.data.model.ApiResult

interface CommunityRepository {
    suspend fun getFeeds(): ApiResult<Feeds>?
    suspend fun getFeedsByTag(tag: String): ApiResult<Feeds>?

    suspend fun searchFeedsByKeyword(keyword: String): ApiResult<Feeds>?
}
