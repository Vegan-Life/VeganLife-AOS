package com.project.veganlife.community.domain.usecase

import com.project.veganlife.community.data.model.CreateResponse
import com.project.veganlife.community.domain.repository.CommunityRepository
import com.project.veganlife.data.model.ApiResult
import javax.inject.Inject

class CreateCommentUseCase @Inject constructor(private val repository: CommunityRepository) {
    suspend fun execute(postId: Long, commentId: Long?, content: String): ApiResult<CreateResponse> {
        return repository.createComment(postId, commentId, content)
    }
}