package com.project.veganlife.community.data.repositoryimpl

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.google.gson.GsonBuilder
import com.project.veganlife.community.data.local.RecentSearchDataStoreManager
import com.project.veganlife.community.data.model.CommentRequest
import com.project.veganlife.community.data.model.CreateResponse
import com.project.veganlife.community.data.model.PopularTagsResponse
import com.project.veganlife.community.data.model.Post
import com.project.veganlife.community.data.model.PostPreview
import com.project.veganlife.community.data.model.PostResponse
import com.project.veganlife.community.data.remote.CommunityApi
import com.project.veganlife.community.data.remote.CommunityFeedPagingSource
import com.project.veganlife.community.data.remote.KeywordFilteredFeedPagingSource
import com.project.veganlife.community.domain.repository.CommunityRepository
import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.data.model.ConflictResponse
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class CommunityRepositoryImpl @Inject constructor(
    private val communityApi: CommunityApi,
    private val recentSearchDataStoreManager: RecentSearchDataStoreManager,
) : CommunityRepository {
    override suspend fun getFeeds(): Flow<PagingData<PostPreview>> {
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = {
                CommunityFeedPagingSource(
                    communityApi
                )
            }
        ).flow
    }

    override suspend fun getFeedsByTag(tag: String): Flow<PagingData<PostPreview>> {
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = {
                KeywordFilteredFeedPagingSource(
                    tag,
                    communityApi
                )
            }
        ).flow
    }

    override suspend fun searchFeedsByKeyword(keyword: String): Flow<PagingData<PostPreview>> {
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = {
                KeywordFilteredFeedPagingSource(
                    keyword,
                    communityApi
                )
            }
        ).flow
    }

    override suspend fun saveRecentSearches(recentSearches: List<String>) {
        recentSearchDataStoreManager.saveRecentSearch(recentSearches)
    }

    override fun getRecentSearches(): Flow<List<String>> {
        return recentSearchDataStoreManager.recentSearch
    }

    override suspend fun getPopularityTags(): ApiResult<PopularTagsResponse> {
        val gson = GsonBuilder().create()

        return try {
            val popularTagsGetResponse = communityApi.getPopularTags()
            if (popularTagsGetResponse.isSuccessful == true) {
                val responseBody = popularTagsGetResponse.body()!!

                ApiResult.Success(responseBody)
            } else {
                val errorBodyString = popularTagsGetResponse.errorBody()?.string()
                val conflictResponse =
                    gson.fromJson(errorBodyString, ConflictResponse::class.java)
                ApiResult.Error(conflictResponse.errorCode, conflictResponse.description)
            }

        } catch (e: Exception) {
            ApiResult.Exception(e)
        }
    }

    override suspend fun getKeywordAutoComplete(keyword: String, size: Int): ApiResult<List<String>> {
        val gson = GsonBuilder().create()

        return try {
            val keywordAutoCompleteResponse = communityApi.keywordAutoComplete(keyword, size)

            
            if (keywordAutoCompleteResponse.isSuccessful == true) {
                val responseBody = keywordAutoCompleteResponse.body()!!
                Log.i("##INFO", "getKeywordAutoComplete: $responseBody")
                ApiResult.Success(responseBody)
            } else {
                val errorBodyString = keywordAutoCompleteResponse.errorBody()?.string()
                val conflictResponse =
                    gson.fromJson(errorBodyString, ConflictResponse::class.java)
                ApiResult.Error(conflictResponse.errorCode, conflictResponse.description)
            }

        } catch (e: Exception) {
            ApiResult.Exception(e)
        }
    }

    override suspend fun getPostData(postId: Int): ApiResult<Post> {

        val gson = GsonBuilder().create()

        return try {
            val getPostResponse = communityApi.getPost(
                postId
            )

            if (getPostResponse.isSuccessful) {
                ApiResult.Success(getPostResponse.body()!!)
            } else {
                val errorBodyString = getPostResponse.errorBody()?.string()
                val conflictResponse =
                    gson.fromJson(errorBodyString, ConflictResponse::class.java)
                ApiResult.Error(conflictResponse.errorCode, conflictResponse.description)
            }

        } catch (e: Exception) {
            ApiResult.Exception(e)
        }
    }

    override suspend fun likePost(postId: Int): ApiResult<Boolean> {
        val gson = GsonBuilder().create()

        return try {
            val likePostResponse = communityApi.likePost(postId)
            if (likePostResponse.isSuccessful == true) {
                ApiResult.Success(true)
            } else {
                val errorBodyString = likePostResponse.errorBody()?.string()
                val conflictResponse =
                    gson.fromJson(errorBodyString, ConflictResponse::class.java)
                ApiResult.Error(conflictResponse.errorCode, conflictResponse.description)
            }

        } catch (e: Exception) {
            ApiResult.Exception(e)
        }
    }

    override suspend fun unlikePost(postId: Int): ApiResult<Boolean> {
        val gson = GsonBuilder().create()

        return try {
            val likePostResponse = communityApi.unlikePost(postId)
            if (likePostResponse.isSuccessful == true) {
                ApiResult.Success(true)
            } else {
                val errorBodyString = likePostResponse.errorBody()?.string()
                val conflictResponse =
                    gson.fromJson(errorBodyString, ConflictResponse::class.java)
                ApiResult.Error(conflictResponse.errorCode, conflictResponse.description)
            }

        } catch (e: Exception) {
            ApiResult.Exception(e)
        }
    }

    override suspend fun createComment(postId: Long, commentId: Long?, content: String): ApiResult<CreateResponse> {
        val gson = GsonBuilder().create()

        return try {
            val commentRequest = CommentRequest(commentId, content)
            val createPostResponse = communityApi.createComment(postId, commentRequest)
            if (createPostResponse.isSuccessful == true) {
                ApiResult.Success(createPostResponse.body()!!)
            } else {
                val errorBodyString = createPostResponse.errorBody()?.string()
                val conflictResponse =
                    gson.fromJson(errorBodyString, ConflictResponse::class.java)
                ApiResult.Error(conflictResponse.errorCode, conflictResponse.description)
            }

        } catch (e: Exception) {
            ApiResult.Exception(e)
        }
    }

    override suspend fun createPost(
        postDTO: RequestBody,
        images: List<MultipartBody.Part>
    ): ApiResult<PostResponse> {
        val gson = GsonBuilder().create()

        return try {
            Log.i("##INFO", "멀티멀티파트파트: ${images}")
            val createPostResponse = communityApi.createPost(postDTO, images)
            if (createPostResponse.isSuccessful == true) {
                val responseBody = createPostResponse.body()!!

                ApiResult.Success(responseBody)
            } else {
                val errorBodyString = createPostResponse.errorBody()?.string()
                val conflictResponse =
                    gson.fromJson(errorBodyString, ConflictResponse::class.java)
                ApiResult.Error(conflictResponse.errorCode, conflictResponse.description)
            }

        } catch (e: Exception) {
            ApiResult.Exception(e)
        }
    }
}
