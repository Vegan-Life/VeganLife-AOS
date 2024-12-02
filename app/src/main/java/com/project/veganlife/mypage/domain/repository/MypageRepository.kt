package com.project.veganlife.mypage.domain.repository

import androidx.paging.PagingData
import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.data.model.ProfileResponse
import com.project.veganlife.mypage.data.model.MyPostedContent
import com.project.veganlife.mypage.data.model.ScrapedRecipeContent
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface MypageRepository {
    suspend fun getMyPosted(type: String): Flow<PagingData<MyPostedContent>>

    suspend fun getScrapedRecipe(): Flow<PagingData<ScrapedRecipeContent>>

    suspend fun deleteWithdrawal(): ApiResult<Any>

    suspend fun modifyProfile(
        profileRequestDTO: RequestBody,
        profilePhotoMultipart: MultipartBody.Part
    ): ApiResult<ProfileResponse>
}