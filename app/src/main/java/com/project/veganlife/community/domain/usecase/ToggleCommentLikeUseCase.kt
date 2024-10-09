package com.project.veganlife.community.domain.usecase

import com.project.veganlife.community.domain.repository.CommunityRepository
import com.project.veganlife.data.model.ApiResult
import javax.inject.Inject

class ToggleCommentLikeUseCase @Inject constructor(private val repository: CommunityRepository) {
    suspend fun execute(postId: Long, commentId: Long): ApiResult<Boolean> {
        return repository.toggleCommentLike(postId, commentId)
    }
}
