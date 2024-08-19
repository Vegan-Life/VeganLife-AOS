package com.project.veganlife.mypage.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.project.veganlife.R
import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.domain.usecase.RecipeLikeUsecase
import com.project.veganlife.mypage.data.model.RecipeLikeState
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
    private val recipeLikeUsecase: RecipeLikeUsecase,

) : ViewModel() {

    private var _scrapedRecipe = MutableStateFlow<PagingData<ScrapedRecipeContent>?>(null)
    val scrapedRecipe: StateFlow<PagingData<ScrapedRecipeContent>?> get() = _scrapedRecipe

    private var _recipeLikeResponse = MutableLiveData<RecipeLikeState>()
    val recipeLikeResponse: LiveData<RecipeLikeState> get() = _recipeLikeResponse

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

    fun like_likeCancelRecipe(recipe: ScrapedRecipeContent) {
        viewModelScope.launch {
            if(recipe.isLiked) {
                try {
                    val response = recipeLikeUsecase.recipeLikeCancel(recipe.recipeId)
                    when(response) {
                        is ApiResult.Error -> Log.d("recipe liekCancel Cacnel Error", response.description)
                        is ApiResult.Exception -> Log.d("recipe liekCancel Exception", response.e.toString())
                        is ApiResult.Success -> _recipeLikeResponse.value = RecipeLikeState(recipe.recipeId, recipe.isLiked)
                    }
                } catch (e: Exception) {
                    Log.d("recipe liekCancel Exception","Exception")
                }
            }
            else {
                try {
                    val response = recipeLikeUsecase.recipeLike(recipe.recipeId)
                    when(response){
                        is ApiResult.Error ->  Log.d("recipe like Error", response.description)
                        is ApiResult.Exception -> Log.d("recipe liek Exception", response.e.toString())
                        is ApiResult.Success -> _recipeLikeResponse.value = RecipeLikeState(recipe.recipeId, recipe.isLiked)
                    }
                } catch (e: Exception) {
                    Log.d("recipe like Exception","Exception")
                }
            }
        }
    }
}
