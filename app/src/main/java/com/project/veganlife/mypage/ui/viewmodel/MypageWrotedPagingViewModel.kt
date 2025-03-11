package com.project.veganlife.mypage.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.project.veganlife.mypage.domain.usecase.MypageUsecase
import com.project.veganlife.recipe.data.model.RecipeFeedContent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MypageWrotedPagingViewModel @Inject constructor(
    private val mypageUsecase: MypageUsecase,
) : ViewModel() {

    private var _wrotedRecipe = MutableStateFlow<PagingData<RecipeFeedContent>>(PagingData.empty())
    val wrotedRecipe: StateFlow<PagingData<RecipeFeedContent>> get() = _wrotedRecipe

    fun getWrotedRecipe() {
        viewModelScope.launch {
            try {
                mypageUsecase.getWrotedRecipe()
                    .cachedIn(viewModelScope)
                    .collectLatest { pagingData ->
                        _wrotedRecipe.value = pagingData
                    }
            } catch (e: Exception) {
                Log.d("ViewModel", "paging error: ${e.message}")
            }
        }
    }
}
