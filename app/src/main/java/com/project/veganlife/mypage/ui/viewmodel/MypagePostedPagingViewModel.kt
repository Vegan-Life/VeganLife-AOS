package com.project.veganlife.mypage.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.project.veganlife.mypage.data.model.MyPostedContent
import com.project.veganlife.mypage.domain.usecase.MypageGetPostedUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MypagePostedPagingViewModel @Inject constructor(
    private val usecase: MypageGetPostedUsecase,
) : ViewModel() {

    private var _posted = MutableStateFlow<PagingData<MyPostedContent>?>(null)
    val posted: StateFlow<PagingData<MyPostedContent>?> get() = _posted

    fun getPostedFeed(type: String) {
        viewModelScope.launch {
            try {
                usecase(type)
                    .collectLatest { pagingData ->
                        _posted.value = pagingData
                    }
            } catch (e: Exception) {
                Log.d("ViewModel", "paging error: ${e.message}")
            }
        }
    }
}
