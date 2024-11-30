package com.project.veganlife.community.ui.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.veganlife.community.data.model.Post
import com.project.veganlife.community.domain.usecase.CreateCommentUseCase
import com.project.veganlife.community.domain.usecase.GetPostDataUseCase
import com.project.veganlife.community.domain.usecase.LikePostUseCase
import com.project.veganlife.community.domain.usecase.UnlikePostUseCase
import com.project.veganlife.data.model.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val getPostDataUseCase: GetPostDataUseCase,
    private val likePostUseCase: LikePostUseCase,
    private val unlikePostUseCase: UnlikePostUseCase,
    private val createCommentUseCase: CreateCommentUseCase
) : ViewModel() {
    val post = MutableLiveData<ApiResult<Post>>()

    fun getPost(postId: Int) {
        viewModelScope.launch {
            post.value = getPostDataUseCase.execute(postId)
        }
    }

    fun likePost(postId: Int) {
        viewModelScope.launch {
            likePostUseCase.execute(postId)
        }
    }

    fun unlikePost(postId: Int) {
        viewModelScope.launch {
            unlikePostUseCase.execute(postId)
        }
    }

    fun createComment(postId: Long, commentId: Long?, comment: String) {
        //직접 댓글이면 commentId는 -1
        // 대댓글이면 commentId는 > 0
        viewModelScope.launch {
            Log.i("##INFO", "createComment: $postId, $commentId, $comment")
            val result = createCommentUseCase.execute(postId, commentId, comment)
            Log.i("##INFO", "createComment: $result")
            if (result is ApiResult.Success) {
        }
    }

    fun updateComment(postId: Long, commentId: Long, comment: String) {

    }

    fun deleteComment(postId: Long, commentId: Long) {

    }
}