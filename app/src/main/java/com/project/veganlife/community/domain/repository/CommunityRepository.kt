package com.project.veganlife.community.domain.repository

import androidx.paging.PagingData
import com.project.veganlife.community.data.model.CreateResponse
import com.project.veganlife.community.data.model.PopularTagsResponse
import com.project.veganlife.community.data.model.Post
import com.project.veganlife.community.data.model.PostPreview
import com.project.veganlife.community.data.model.PostResponse
import com.project.veganlife.data.model.ApiResult
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface CommunityRepository {
    suspend fun getFeeds(): Flow<PagingData<PostPreview>>
    suspend fun getFeedsByTag(tag: String): Flow<PagingData<PostPreview>>

    suspend fun searchFeedsByKeyword(keyword: String): Flow<PagingData<PostPreview>>

    suspend fun saveRecentSearches(recentSearches: List<String>)

    fun getRecentSearches(): Flow<List<String>>

    suspend fun getPopularityTags(): ApiResult<PopularTagsResponse>

    suspend fun getKeywordAutoComplete(keyword: String, size: Int): ApiResult<List<String>>
    suspend fun getPostData(postId: Int): ApiResult<Post>

    suspend fun likePost(postId: Int): ApiResult<Boolean>

    suspend fun unlikePost(postId: Int): ApiResult<Boolean>

    suspend fun createComment(postId: Long, commentId: Long?, content: String): ApiResult<CreateResponse>

    suspend fun createPost(postDTO: RequestBody, images: List<MultipartBody.Part>): ApiResult<PostResponse>

    suspend fun deletePost(postId: Int): ApiResult<Boolean>

    suspend fun updatePost(postId: Int, postDTO: RequestBody, images: List<MultipartBody.Part>): ApiResult<Boolean>
}
