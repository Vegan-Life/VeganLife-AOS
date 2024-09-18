package com.project.veganlife.community.data.remote

import com.project.veganlife.community.data.model.Feed
import com.project.veganlife.data.model.PagingResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CommunityApi {
    @GET("posts")
    suspend fun getAllFeeds(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("sort") sort: String,
    ): Response<PagingResponse<Feed>>

    @GET("posts/search")
    suspend fun searchFeedByKeyword(
        @Query("keyword") keyword: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("sort") sort: String,
    ): Response<PagingResponse<Feed>>
}
