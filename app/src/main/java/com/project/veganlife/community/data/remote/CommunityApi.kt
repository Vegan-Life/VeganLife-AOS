package com.project.veganlife.community.data.remote

import com.project.veganlife.community.data.model.CommentRequest
import com.project.veganlife.community.data.model.CreateResponse
import com.project.veganlife.community.data.model.PopularTagsResponse
import com.project.veganlife.community.data.model.Post
import com.project.veganlife.community.data.model.PostPreview
import com.project.veganlife.data.model.PagingResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
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

    @POST("posts/{postId}/likes")
    suspend fun likePost(
        @Path("postId") postId: Int
    ): Response<String>

    @DELETE("posts/{postId}/likes")
    suspend fun unlikePost(
        @Path("postId") postId: Int
    ): Response<String>

    @POST("posts/{postId}/comments")
    suspend fun createComment(
        @Path("postId") postId: Long,
        @Body commentRequest: CommentRequest
    ): Response<CreateResponse>

    @Multipart
    @POST("posts")
    suspend fun createPost(
        @Part("request") postDTO: RequestBody,
        @Part images: List<MultipartBody.Part>
    ): Response<CreateResponse>
}
