package com.project.veganlife.mypage.domain.repository

import androidx.paging.PagingData
import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.data.model.ProfileResponse
import com.project.veganlife.mypage.data.model.MyPostedContent
import com.project.veganlife.recipe.data.model.RecipeFeedContent
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface MypageRepository {
    suspend fun getMyWroted(type: String): Flow<PagingData<MyPostedContent>>

    suspend fun getScrapedRecipe(): Flow<PagingData<RecipeFeedContent>>

    suspend fun deleteWithdrawal(): ApiResult<Any>

    suspend fun modifyProfile(
        profileRequestDTO: RequestBody,
        profilePhotoMultipart: MultipartBody.Part?
    ): ApiResult<ProfileResponse>

    suspend fun getWrotedRecipe(): Flow<PagingData<RecipeFeedContent>>
}