package com.project.veganlife.community.data.remote

import com.project.veganlife.community.data.model.Feeds
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface CommunityApi {
    @GET("posts")
    suspend fun getFeedAll(
        @Header("Authorization") accessToken: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("sort") sort: String,
    ): Response<Feeds>

    @GET("posts/search")
    suspend fun searchFeedByKeyword(
        @Header("Authorization") accessToken: String,
        @Query("keyword") keyword: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("sort") sort: String,
    ): Response<Feeds>
}
