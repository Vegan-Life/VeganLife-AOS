package com.project.veganlife.mypage.data.repositoryImpl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.project.veganlife.mypage.data.datasource.PostedCommentPagingSource
import com.project.veganlife.mypage.data.datasource.PostedFeedPagingSource
import com.project.veganlife.mypage.data.model.MyPostedContent
import com.project.veganlife.mypage.data.remote.MypageGetMyPostedCommentApi
import com.project.veganlife.mypage.data.remote.MypageGetMyPostedFeedApi
import com.project.veganlife.mypage.domain.repository.MypagePostedRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MypagePostedRepositoryImpl @Inject constructor(
    private val mypageGetMyPostedFeedApi: MypageGetMyPostedFeedApi,
    private val mypageGetMyPostedCommentApi: MypageGetMyPostedCommentApi,
): MypagePostedRepository {
    // getPostedFeeds 함수는 Pager를 통해 PagingData의 Flow를 반환합니다.
    // Pager는 페이징 데이터를 로드하고 관리하는 역할을 합니다.
    override suspend fun getMyPosted(type: String): Flow<PagingData<MyPostedContent>> {
        return when(type) {
            "feed" ->
                Pager(
                config = PagingConfig(pageSize = 20, enablePlaceholders = false),
                pagingSourceFactory = { PostedFeedPagingSource(mypageGetMyPostedFeedApi) }
            ).flow // Flow로 변환하여 반환합니다.

            else ->
                Pager(
                config = PagingConfig(pageSize = 20, enablePlaceholders = false),
                pagingSourceFactory = { PostedCommentPagingSource(mypageGetMyPostedCommentApi) }
            ).flow // Flow로 변환하여 반환합니다.
        }
    }
}
