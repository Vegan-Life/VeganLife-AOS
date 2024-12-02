package com.project.veganlife.mypage.data.remote

import com.project.veganlife.data.model.PagingResponse
import com.project.veganlife.data.model.ProfileResponse
import com.project.veganlife.mypage.data.model.MyPostedContent
import com.project.veganlife.mypage.data.model.ScrapedRecipeContent
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Query

interface MypageApi {
    @GET("members/me/posts-with-comments")
    suspend fun getMyCommentList(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("sort") sort: String,
    ): Response<PagingResponse<MyPostedContent>>

    @GET("members/me/posts")
    suspend fun getMyPostedFeedList(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("sort") sort: String,
    ): Response<PagingResponse<MyPostedContent>>

    @GET("members/me/liked-recipes")
    suspend fun getMyScrapedRecipeList(
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): Response<PagingResponse<ScrapedRecipeContent>>

    @DELETE("members")
    suspend fun deleteWithdrawal(
    ): Response<Any>

    @Multipart
    @PUT("members/profile")
    suspend fun modifyProfile(
        @Part("request") ProfileRequestDTO: RequestBody,
        @Part image: MultipartBody.Part?
    ): Response<ProfileResponse>
}