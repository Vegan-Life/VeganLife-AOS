package com.project.veganlife.community.domain.usecase

import com.project.veganlife.community.data.model.PopularTagsResponse
import com.project.veganlife.community.domain.repository.CommunityRepository
import com.project.veganlife.data.model.ApiResult
import javax.inject.Inject

class GetPopularTagsUseCase @Inject constructor(private val repository: CommunityRepository) {
    suspend fun execute(): ApiResult<PopularTagsResponse> {
        return repository.getPopularityTags()
    }
}