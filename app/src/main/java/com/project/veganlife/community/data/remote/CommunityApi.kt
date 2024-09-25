package com.project.veganlife.community.data.remote

import com.project.veganlife.community.data.model.PostPreview
import com.project.veganlife.community.data.model.PopularTagsResponse
import com.project.veganlife.community.data.model.Post
import com.project.veganlife.data.model.PagingResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CommunityApi {
    @GET("posts")
    suspend fun getAllFeeds(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("sort") sort: String,
    ): Response<PagingResponse<PostPreview>>

    @GET("posts/search")
    suspend fun searchFeedByKeyword(
        @Query("keyword") keyword: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("sort") sort: String,
    ): Response<PagingResponse<PostPreview>>

    @GET("posts/tags")
    suspend fun getPopularTags(
    ): Response<PopularTagsResponse>

    @GET("posts/{postId}")
    suspend fun getPost(
        @Path("postId") postId: Int,
    ): Response<Post>
}
