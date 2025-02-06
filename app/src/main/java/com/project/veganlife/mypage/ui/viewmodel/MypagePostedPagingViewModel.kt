package com.project.veganlife.mypage.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.project.veganlife.mypage.data.model.MyPostedContent
import com.project.veganlife.mypage.domain.usecase.MypageUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MypagePostedPagingViewModel @Inject constructor(
    private val mypageUsecase: MypageUsecase,
) : ViewModel() {

    private var _posted = MutableStateFlow<PagingData<MyPostedContent>>(PagingData.empty())
    val posted: StateFlow<PagingData<MyPostedContent>> get() = _posted

    fun getPostedFeed(type: String) {
        viewModelScope.launch {
            try {
                mypageUsecase.getMyPosted(type)
                    .cachedIn(viewModelScope)
                    .collectLatest { pagingData ->
                        _posted.value = pagingData
                    }
            } catch (e: Exception) {
                Log.d("ViewModel", "paging error: ${e.message}")
            }
        }
    }
}
