package com.project.veganlife.community.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.veganlife.community.data.model.Post
import com.project.veganlife.community.domain.usecase.GetPostDataUseCase
import com.project.veganlife.data.model.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val getPostDataUseCase: GetPostDataUseCase
): ViewModel() {
    val post = MutableLiveData<ApiResult<Post>>()

    fun getPost(postId: Int) {
        viewModelScope.launch {
            post.value = getPostDataUseCase.execute(postId)
        }
    }
}