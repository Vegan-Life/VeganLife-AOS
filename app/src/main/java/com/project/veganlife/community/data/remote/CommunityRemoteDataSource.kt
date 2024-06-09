package com.project.veganlife.community.data.remote

import com.google.gson.GsonBuilder
import com.project.veganlife.community.data.model.Feeds
import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.data.model.ConflictResponse
import javax.inject.Inject

class CommunityRemoteDataSource @Inject constructor(
    private val communityApi: CommunityApi
) {
    suspend fun getFeeds(
        accessToken: String,
    ): ApiResult<Feeds> {
        val gson = GsonBuilder().create()

        try {
            val feedsGetResponse = communityApi.getFeedAll(
                accessToken,
                0,
                30,
                "createdAt,DESC",
            )

            if (!feedsGetResponse.isSuccessful) {
                val errorBodyString = feedsGetResponse.errorBody()?.string()
                val conflictResponse = gson.fromJson(errorBodyString, ConflictResponse::class.java)

                return ApiResult.Error(conflictResponse.errorCode, conflictResponse.description)
            }

            return ApiResult.Success(feedsGetResponse.body()!!)
        } catch (e: Exception) {
            return ApiResult.Exception(e)
        }
    }

    suspend fun searchFeedsByKeyword(accessToken: String, keyword: String): ApiResult<Feeds> {
        val gson = GsonBuilder().create()

        try {
            val feedsGetResponse = communityApi.searchFeedByKeyword(
                accessToken,
                keyword,
                0,
                30,
                "createdAt,DESC",
            )

            if (!feedsGetResponse.isSuccessful) {
                val errorBodyString = feedsGetResponse.errorBody()?.string()
                val conflictResponse = gson.fromJson(errorBodyString, ConflictResponse::class.java)

                return ApiResult.Error(conflictResponse.errorCode, conflictResponse.description)
            }

            return ApiResult.Success(feedsGetResponse.body()!!)
        } catch (e: Exception) {
            return ApiResult.Exception(e)
        }
    }
}
