package com.project.veganlife.mypage.data.remote

import retrofit2.Response
import retrofit2.http.DELETE

interface MypageWithDrawalApi {
    @DELETE("members")
    suspend fun deleteWithdrawal(
    ): Response<Any>
}