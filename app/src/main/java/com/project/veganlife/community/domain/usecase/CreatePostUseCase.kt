package com.project.veganlife.community.domain.usecase

import com.project.veganlife.community.data.model.CreateResponse
import com.project.veganlife.community.domain.repository.CommunityRepository
import com.project.veganlife.data.model.ApiResult
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class CreatePostUseCase @Inject constructor(private val repository: CommunityRepository) {
    suspend fun execute(postDTO: RequestBody, images: List<MultipartBody.Part>): ApiResult<CreateResponse> {
        return repository.createPost(postDTO, images)
    }
}