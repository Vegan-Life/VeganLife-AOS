package com.project.veganlife.mypage.domain.usecase

import androidx.paging.PagingData
import com.project.veganlife.mypage.data.model.ScrapedRecipeContent
import com.project.veganlife.mypage.domain.repository.MypageScrapedRecipeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MypageGetScrapedRecipeUsecase @Inject constructor(
    val mypageScrapedRecipeRepository: MypageScrapedRecipeRepository,
) {
    suspend operator fun invoke(): Flow<PagingData<ScrapedRecipeContent>> {
        return mypageScrapedRecipeRepository.getScrapedRecipe()
    }
}