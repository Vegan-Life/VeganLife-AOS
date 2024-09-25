package com.project.veganlife.mypage.data.remote

import com.project.veganlife.data.model.PagingResponse
import com.project.veganlife.mypage.data.model.MyPostedContent
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MypageGetMyPostedCommentApi {
    @GET("members/me/posts-with-comments")
    suspend fun getMyCommentList(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("sort") sort: String,
    ): Response<PagingResponse<MyPostedContent>>
}