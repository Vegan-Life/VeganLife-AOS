package com.project.veganlife.community.ui.viewmodel

import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.veganlife.community.data.model.PopularTagsResponse
import com.project.veganlife.community.data.model.PostDTO
import com.project.veganlife.community.data.model.PostResponse
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
    private val _response = MutableLiveData<ApiResult<PostResponse>>()
    val response: LiveData<ApiResult<PostResponse>> get() = _response

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

    fun addKeyword(newString: String) {
        // 기존 리스트 가져오기 (null이면 빈 리스트로 초기화)
        val currentList = keywordList.value ?: emptyList()

        // 새 리스트 생성
        val updatedList = currentList + newString

        // LiveData 업데이트
        _keywordList.value = updatedList
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

    @RequiresApi(Build.VERSION_CODES.R)
    fun createPost(
        context: Context,
        keywords: List<String>,
        title: String,
        content: String,
        images: List<Uri>
    ) {
        val postRequestBody = createPostRequestBody(keywords, title, content)

        val imagesMultipart = mutableListOf<MultipartBody.Part>()
        if (images.isNotEmpty()) {
            images.forEach { uri ->
                // 1. 최적화된 비트맵을 임시 파일로 저장
                val imagePath = PhotoUtils.optimizeBitmap(context, uri)
                PhotoUtils.createImageMultipart(imagePath)?.let {
                    // 2. 임시 파일 경로를 사용해 MultipartBody.Part로 변환
                    imagesMultipart.add(it)
                }
            }
        }

        viewModelScope.launch {
            _response.value = createPostUseCase.execute(postRequestBody, imagesMultipart)
        }
    }

    private fun createPostRequestBody(
        keywords: List<String>,
        title: String,
        content: String
    ): RequestBody {
        val postDTO = PostDTO(title, content, keywords)

        return PhotoUtils.createRequestBody(postDTO)
    }

    fun setImageUris(uris: List<Uri>) {
        val uriList = mutableListOf<Uri>()
        imageUris.value?.let { uriList.addAll(it) }
        uriList.addAll(uris)

        _imageUris.value = uris
    }

    fun addUriList(newUris: List<Uri>) {
        val currentList = imageUris.value ?: emptyList()

        val updatedList = currentList + newUris

        _imageUris.value = updatedList
    }

    fun removePartAt(position: Int) {
        val modifiedImageUris = mutableListOf<Uri>()
        imageUris.value?.let { modifiedImageUris.addAll(it) }

        modifiedImageUris.removeAt(position)
        setImageUris(modifiedImageUris)
    }
}