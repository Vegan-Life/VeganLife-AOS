package com.project.veganlife.community.domain.usecase

import com.project.veganlife.community.domain.repository.CommunityRepository
import com.project.veganlife.data.model.ApiResult
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class UpdatePostUseCase @Inject constructor(private val repository: CommunityRepository) {
    suspend fun execute(postId: Int, postDTO: RequestBody, images: List<MultipartBody.Part>): ApiResult<Boolean> {
        return repository.updatePost(postId, postDTO, images)
    }

}