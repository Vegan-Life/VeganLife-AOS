package com.project.veganlife.mypage.data.repositoryImpl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.data.model.ProfileResponse
import com.project.veganlife.mypage.data.datasource.MypageWithdrawalDataSourceImpl
import com.project.veganlife.mypage.data.datasource.MypagePostedCommentPagingSource
import com.project.veganlife.mypage.data.datasource.MypagePostedFeedPagingSource
import com.project.veganlife.mypage.data.datasource.MypageProfileModifyDataSourceImpl
import com.project.veganlife.mypage.data.datasource.MypageScrapedRecipePagingSource
import com.project.veganlife.mypage.data.model.MyPostedContent
import com.project.veganlife.mypage.data.model.ScrapedRecipeContent
import com.project.veganlife.mypage.data.remote.MypageApi
import com.project.veganlife.mypage.domain.repository.MypageRepository
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class MypageRepositoryImpl @Inject constructor(
    private val mypageApi: MypageApi,
    private val mypageWithdrawalDataSourceImpl: MypageWithdrawalDataSourceImpl,
    private val mypageProfileModifyDataSourceImpl: MypageProfileModifyDataSourceImpl,
) : MypageRepository{
    override suspend fun getMyPosted(type: String): Flow<PagingData<MyPostedContent>> {
        return when(type) {
            "feed" ->
                Pager(
                    config = PagingConfig(pageSize = 20, enablePlaceholders = false),
                    pagingSourceFactory = {
                        MypagePostedFeedPagingSource(mypageApi) }
                ).flow // Flow로 변환하여 반환합니다.

            else ->
                Pager(
                    config = PagingConfig(pageSize = 20, enablePlaceholders = false),
                    pagingSourceFactory = { MypagePostedCommentPagingSource(mypageApi) }
                ).flow // Flow로 변환하여 반환합니다.
        }
    }

    override suspend fun getScrapedRecipe(): Flow<PagingData<ScrapedRecipeContent>> {
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = {
                MypageScrapedRecipePagingSource(
                    mypageApi,
                )
            }
        ).flow // Flow로 변환하여 반환합니다.
    }

    override suspend fun deleteWithdrawal(): ApiResult<Any> {
        return mypageWithdrawalDataSourceImpl.deleteWithdrawal()
    }

    override suspend fun modifyProfile(
        profileRequestDTO: RequestBody,
        profilePhotoMultipart: MultipartBody.Part
    ): ApiResult<ProfileResponse> {
        return mypageProfileModifyDataSourceImpl.modifyProfile(profileRequestDTO, profilePhotoMultipart)
    }
}