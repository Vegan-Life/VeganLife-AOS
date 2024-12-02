package com.project.veganlife.mypage.domain.usecase

import androidx.paging.PagingData
import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.data.model.ProfileResponse
import com.project.veganlife.mypage.data.model.MyPostedContent
import com.project.veganlife.mypage.data.model.ScrapedRecipeContent
import com.project.veganlife.mypage.domain.repository.MypageRepository
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class MypageUsecase @Inject constructor(
    private val mypageRepository: MypageRepository,
) {
    suspend fun getMyPosted(type: String): Flow<PagingData<MyPostedContent>> {
        return mypageRepository.getMyPosted(type)
    }

    suspend fun getScrapedRecipe(): Flow<PagingData<ScrapedRecipeContent>> {
        return mypageRepository.getScrapedRecipe()
    }

    suspend fun deleteWithdrawal(): ApiResult<Any> {
        return mypageRepository.deleteWithdrawal()
    }

    suspend fun modifyProfile(
        profileModifyDTO: RequestBody,
        profilePhotoMultipart: MultipartBody.Part
    ): ApiResult<ProfileResponse> {
        return mypageRepository.modifyProfile(profileModifyDTO, profilePhotoMultipart)
    }
}