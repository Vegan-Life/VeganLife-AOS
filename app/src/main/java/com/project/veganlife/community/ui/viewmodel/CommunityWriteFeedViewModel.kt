package com.project.veganlife.community.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.veganlife.community.data.model.PostDTO
import com.project.veganlife.community.domain.usecase.CreatePostUseCase
import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.utils.PhotoUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class CommunityWriteFeedViewModel @Inject constructor(
    private val createPostUseCase: CreatePostUseCase
) : ViewModel() {
    //키워드 리스트
    private val _keywordList: MutableLiveData<List<String>> = MutableLiveData(emptyList())
    val keywordList: LiveData<List<String>> get() = _keywordList

    // 프로필 사진 MultiPart
    private val _imagesFile = MutableLiveData<List<File>>()
    val imagesFile: LiveData<List<File>> get() = _imagesFile

    //게시물 등록 결과
    private val _response = MutableLiveData<String>()
    val response: LiveData<String> get() = _response

    //사진
//    private val

//    fun putImagesMultipart(images: List<MultipartBody.Part>) {
//        _imagesFile.value = images
//    }

//    fun putPostRequestBody(post: RequestBody) {
//        _postRequestBody.value = post
//    }

    fun createPost(keywords: List<String>, title: String, content: String, images: List<File>) {
        val postRequestBody = createPostRequestBody(keywords, title, content)
        val imagesMultipart = createImagesMultipart(images)
        viewModelScope.launch {
            when (val response = createPostUseCase.execute(postRequestBody, imagesMultipart)) {
                is ApiResult.Success -> {
                    _response.value = "게시물이 등록됐습니다."
                }

                is ApiResult.Error -> {
                    _response.value = "게시물 등록에 실패했습니다. 다시 시도해주세요."
                    Log.e(
                        "##ERROR",
                        "createPost ERROR: ${response.errorCode}, ${response.description}",
                    )
                }

                is ApiResult.Exception -> {
                    Log.e("##ERROR", "createPost EXCEPTION: ${response.e.stackTraceToString()}")
                    _response.value = "게시물 등록에 실패했습니다. 다시 시도해주세요."
                }
            }
        }
    }

    private fun createImagesMultipart(parts: List<File>): List<MultipartBody.Part> {
        return parts.map { PhotoUtils.createImageMultipart(it.absolutePath)!! }
    }

    private fun createPostRequestBody(keywords: List<String>, title: String, content: String): RequestBody {
        val postDTO = PostDTO(keywords, title, content)

        return PhotoUtils.createRequestBody(postDTO)
    }
}