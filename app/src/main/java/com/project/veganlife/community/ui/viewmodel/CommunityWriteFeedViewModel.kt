package com.project.veganlife.community.ui.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.veganlife.community.data.model.PopularTagsResponse
import com.project.veganlife.community.data.model.PostDTO
import com.project.veganlife.community.domain.usecase.CreatePostUseCase
import com.project.veganlife.community.domain.usecase.GetPopularTagsUseCase
import com.project.veganlife.community.domain.usecase.KeywordAutoCompleteUseCase
import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.utils.PhotoUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class CommunityWriteFeedViewModel @Inject constructor(
    private val createPostUseCase: CreatePostUseCase,
    private val getPopularTagsUseCase: GetPopularTagsUseCase,
    private val keywordAutoCompleteUseCase: KeywordAutoCompleteUseCase,
) : ViewModel() {
    //키워드 리스트
    private val _keywordList: MutableLiveData<List<String>> = MutableLiveData(emptyList())
    val keywordList: LiveData<List<String>> get() = _keywordList

    //인기 태그 리스트
    private val _popularTagList = MutableLiveData<ApiResult<PopularTagsResponse>>()
    val popularTagList: LiveData<ApiResult<PopularTagsResponse>> = _popularTagList

    //연관 키워드 리스트
    private val _keywordAutoCompleteList = MutableLiveData<ApiResult<List<String>>>()
    val keywordAutoCompleteList: LiveData<ApiResult<List<String>>> get() = _keywordAutoCompleteList

    private val _imageUris: MutableLiveData<List<Uri>> = MutableLiveData(emptyList())
    val imageUris: LiveData<List<Uri>> get() = _imageUris

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

    init {
        loadPopularTags()
    }

    fun getKeywordAutoComplete(keyword: String) {
        viewModelScope.launch {
            _keywordAutoCompleteList.value = keywordAutoCompleteUseCase.execute(keyword, 5)
        }
    }

    fun loadPopularTags() {
        viewModelScope.launch {
            _popularTagList.value = getPopularTagsUseCase.execute()
        }
    }

    fun createPost(context: Context, keywords: List<String>, title: String, content: String, images: List<Uri>) {
        val postRequestBody = createPostRequestBody(keywords, title, content)

        val imagesMultipart = mutableListOf<MultipartBody.Part>()
        if (images.isNotEmpty()) {
            images.forEach { uri ->
                PhotoUtils.uriToMultipart(uri, context)?.let { it1 -> imagesMultipart.add(it1) }
            }
        }

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

    private fun createPostRequestBody(keywords: List<String>, title: String, content: String): RequestBody {
        val postDTO = PostDTO(keywords, title, content)

        return PhotoUtils.createRequestBody(postDTO)
    }

    fun setImageUris(uris: List<Uri>) {
        _imageUris.value = uris
    }

    fun removePartAt(position: Int) {
        val modifiedImageUris = mutableListOf<Uri>()
        imageUris.value?.let { modifiedImageUris.addAll(it) }

        modifiedImageUris.removeAt(position)
        setImageUris(modifiedImageUris)
    }
}