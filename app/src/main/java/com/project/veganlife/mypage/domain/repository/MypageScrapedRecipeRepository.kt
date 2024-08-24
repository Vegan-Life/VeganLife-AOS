package com.project.veganlife.mypage.domain.repository

import androidx.paging.PagingData
import com.project.veganlife.mypage.data.model.ScrapedRecipeContent
import kotlinx.coroutines.flow.Flow

interface MypageScrapedRecipeRepository {
    suspend fun getScrapedRecipe(): Flow<PagingData<ScrapedRecipeContent>>
}
