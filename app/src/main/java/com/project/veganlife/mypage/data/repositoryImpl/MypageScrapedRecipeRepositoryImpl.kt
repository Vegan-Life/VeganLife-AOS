package com.project.veganlife.mypage.data.repositoryImpl

import android.content.SharedPreferences
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.project.veganlife.mypage.data.datasource.ScrapedRecipePagingSource
import com.project.veganlife.mypage.data.model.ScrapedRecipeContent
import com.project.veganlife.mypage.data.remote.MypageGetScrapedRecipeApi
import com.project.veganlife.mypage.domain.repository.MypageScrapedRecipeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MypageScrapedRecipeRepositoryImpl @Inject constructor(
    private val mypageGetScrapedRecipeApi: MypageGetScrapedRecipeApi,
    private val sharedPreferences: SharedPreferences,
) : MypageScrapedRecipeRepository {
    // getPostedFeeds 함수는 Pager를 통해 PagingData의 Flow를 반환합니다.
    // Pager는 페이징 데이터를 로드하고 관리하는 역할을 합니다.
    override suspend fun getScrapedRecipe(): Flow<PagingData<ScrapedRecipeContent>> {
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = {
                ScrapedRecipePagingSource(
                    mypageGetScrapedRecipeApi,
                    sharedPreferences
                )
            }
        ).flow // Flow로 변환하여 반환합니다.
    }
}
