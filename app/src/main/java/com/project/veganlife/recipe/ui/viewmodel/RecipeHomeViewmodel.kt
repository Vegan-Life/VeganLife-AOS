package com.project.veganlife.recipe.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.project.veganlife.recipe.data.model.RecipeFeedContent
import com.project.veganlife.recipe.domain.usecase.RecipeUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeHomeViewmodel @Inject constructor(
    val recipeUsecase: RecipeUsecase,
) : ViewModel() {
    private val _recipeFeedList = MutableStateFlow<PagingData<RecipeFeedContent>?>(null)
    val recipeFeedList: StateFlow<PagingData<RecipeFeedContent>?> get() = _recipeFeedList

    fun getAllRecipeFeedsList() {
        viewModelScope.launch {
            try {
                recipeUsecase.getRecipeAllFeeds()
                    .cachedIn(viewModelScope)
                    .collectLatest { pagingData ->
                        _recipeFeedList.value = pagingData
                    }
            } catch (e: Exception) {
                Log.d("ViewModel", "paging error: ${e.message}")
            }
        }
    }

    fun getRecipeFeedByTypeList(type: String) {
        viewModelScope.launch {
            try {
                recipeUsecase.getRecipeFeedByType(type)
                    .cachedIn(viewModelScope)
                    .collectLatest { pagingData ->
                        _recipeFeedList.value = pagingData
                    }
            } catch (e: Exception) {
                Log.d("ViewModel", "paging error: ${e.message}")
            }
        }
    }

}