package com.project.veganlife.community.domain.repository

import com.project.veganlife.community.data.model.Feeds

interface CommunityRepository {
    suspend fun getFeeds(
        accessToken: String,
    ): Feeds?
}
