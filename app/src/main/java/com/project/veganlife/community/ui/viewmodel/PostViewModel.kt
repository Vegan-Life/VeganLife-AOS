package com.project.veganlife.community.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.veganlife.community.data.model.CreateResponse
import com.project.veganlife.community.data.model.Post
import com.project.veganlife.community.domain.usecase.CreateCommentUseCase
import com.project.veganlife.community.domain.usecase.GetPostDataUseCase
import com.project.veganlife.community.domain.usecase.LikePostUseCase
import com.project.veganlife.community.domain.usecase.UnlikePostUseCase
import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.data.model.ProfileResponse
import com.project.veganlife.domain.usecase.ProfileGetUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val getPostDataUseCase: GetPostDataUseCase,
    private val likePostUseCase: LikePostUseCase,
    private val unlikePostUseCase: UnlikePostUseCase,
    private val createCommentUseCase: CreateCommentUseCase,
    private val profileGetUseCase: ProfileGetUsecase
) : ViewModel() {
    val post = MutableLiveData<ApiResult<Post>>()
    val myProfile = MutableLiveData<ApiResult<ProfileResponse>>()


    fun getPost(postId: Int) {
        viewModelScope.launch {
            post.value = getPostDataUseCase.execute(postId)
            myProfile.value = profileGetUseCase.invoke()
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

    fun createComment(postId: Long, commentId: Long?, comment: String, onCommentCreated: (ApiResult<CreateResponse>) -> Unit) {
        //직접 댓글이면 commentId는 -1
        // 대댓글이면 commentId는 > 0
        viewModelScope.launch {
            val result = createCommentUseCase.execute(postId, commentId, comment)
            onCommentCreated(result)
        }
    }

    fun updateComment(postId: Long, commentId: Long, comment: String) {

    }

    fun deleteComment(postId: Long, commentId: Long) {

    }
}