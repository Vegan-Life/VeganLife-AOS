package com.project.veganlife.mypage.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.project.veganlife.mypage.data.model.ScrapedRecipeContent
import com.project.veganlife.mypage.domain.usecase.MypageGetScrapedRecipeUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MypageScrapedRecipeViewModel @Inject constructor(
    private val mypageGetScrapedRecipeUsecase: MypageGetScrapedRecipeUsecase,

) : ViewModel() {

    private var _scrapedRecipe = MutableStateFlow<PagingData<ScrapedRecipeContent>?>(null)
    val scrapedRecipe: StateFlow<PagingData<ScrapedRecipeContent>?> get() = _scrapedRecipe

    fun getScrapedRecipe() {
        viewModelScope.launch {
            try {
                mypageGetScrapedRecipeUsecase()
                    .collectLatest { pagingData ->
                        _scrapedRecipe.value = pagingData
                    }
            } catch (e: Exception) {
                Log.d("ViewModel", "paging error: ${e.message}")
            }
        }
    }
}
