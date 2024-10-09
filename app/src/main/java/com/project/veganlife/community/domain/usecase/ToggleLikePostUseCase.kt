package com.project.veganlife.community.domain.usecase

import com.project.veganlife.community.domain.repository.CommunityRepository
import com.project.veganlife.data.model.ApiResult
import javax.inject.Inject

class ToggleLikePostUseCase @Inject constructor(private val repository: CommunityRepository) {
    suspend fun execute(postId: Int): ApiResult<Boolean> {
        return repository.toggleLikePost(postId)
    }
}
