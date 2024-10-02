package com.project.veganlife.community.data.repositoryimpl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.google.gson.GsonBuilder
import com.project.veganlife.community.data.local.RecentSearchDataStoreManager
import com.project.veganlife.community.data.model.PostPreview
import com.project.veganlife.community.data.model.PopularTagsResponse
import com.project.veganlife.community.data.model.Post
import com.project.veganlife.community.data.remote.CommunityApi
import com.project.veganlife.community.data.remote.CommunityFeedPagingSource
import com.project.veganlife.community.data.remote.KeywordFilteredFeedPagingSource
import com.project.veganlife.community.domain.repository.CommunityRepository
import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.data.model.ConflictResponse
import kotlinx.coroutines.flow.Flow
import java.lang.Exception
import javax.inject.Inject

class CommunityRepositoryImpl @Inject constructor(
    private val communityApi: CommunityApi,
    private val sharedPreferences: SharedPreferences,
    private val recentSearchDataStoreManager: RecentSearchDataStoreManager,
    private val accessToken: SharedPreferences
) : CommunityRepository {
    override suspend fun getFeeds(): Flow<PagingData<PostPreview>> {
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = {
                CommunityFeedPagingSource(
                    communityApi,
                    sharedPreferences
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
                    communityApi,
                    sharedPreferences
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
                    communityApi,
                    sharedPreferences
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
            val popularTagsGetResponse = communityApi.getPopularTags(accessToken.getString("ApiAccessToken", null))
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

    override suspend fun getPostData(postId: Int): ApiResult<Post> {

        val gson = GsonBuilder().create()

        return try {
            val getPostResponse = communityApi.getPost(
                accessToken.getString("ApiAccessToken", null),
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
}
