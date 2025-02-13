package com.project.veganlife.community.ui.viewmodel

import android.content.Context
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.veganlife.community.data.model.ImageItem
import com.project.veganlife.community.data.model.PopularTagsResponse
import com.project.veganlife.community.data.model.Post
import com.project.veganlife.community.data.model.PostDTO
import com.project.veganlife.community.data.model.PostResponse
import com.project.veganlife.community.data.model.PostUpdateDTO
import com.project.veganlife.community.domain.usecase.CreatePostUseCase
import com.project.veganlife.community.domain.usecase.GetPopularTagsUseCase
import com.project.veganlife.community.domain.usecase.KeywordAutoCompleteUseCase
import com.project.veganlife.community.domain.usecase.UpdatePostUseCase
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
    private val updatePostUseCase: UpdatePostUseCase
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

    //fixme: oldimage or newimage
    private val _images = MutableLiveData<MutableList<ImageItem>>(mutableListOf())
    val images: LiveData<MutableList<ImageItem>> = _images

    //게시물 등록 결과
    private val _response = MutableLiveData<ApiResult<PostResponse>>()
    val response: LiveData<ApiResult<PostResponse>> get() = _response

    private val _updateResponse = MutableLiveData<ApiResult<Boolean>>()
    val updateResponse: LiveData<ApiResult<Boolean>> get() = _updateResponse

    private val _oldPost = MutableLiveData<Post>()
    val oldPost: LiveData<Post> get() = _oldPost

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

    fun loadExistingImages(existingUrls: List<String>) {
        val currentList = _images.value ?: mutableListOf()
        currentList.addAll(existingUrls.map { ImageItem(url = it) })
        _images.value = currentList
    }

    fun initOldPostData(post: Post) {
        _oldPost.value = post
        _keywordList.value = post.tags
        Log.d("PostWriteViewModel", "initPostData: ${post.imageUrls}")
        //fixme: 기존 이미지 리스트에 넣기
        loadExistingImages(post.imageUrls)
    }

    fun addImage(uri: Uri) {
        val currentList = _images.value ?: mutableListOf()
        currentList.add(ImageItem(uri = uri))
        _images.value = currentList
    }

    fun removeImage(position: Int) {
        val currentList = _images.value ?: mutableListOf()
        if (position in currentList.indices) {
            currentList.removeAt(position)
            _images.value = currentList
        }
    }

    fun getNewImages(): List<Uri> {
        return _images.value?.mapNotNull { it.uri } ?: emptyList()
    }

    fun getExistingImageUrls(): List<String> {
        return _images.value?.mapNotNull { it.url } ?: emptyList()
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
                PhotoUtils.createImagesMultipart(imagePath)?.let {
                    // 2. 임시 파일 경로를 사용해 MultipartBody.Part로 변환
                    imagesMultipart.add(it)
                }
            }
        }

        viewModelScope.launch {
            _response.value = createPostUseCase.execute(postRequestBody, imagesMultipart)
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    fun updatePost(
        context: Context,
        postId: Int,
        keywords: List<String>,
        title: String,
        content: String,
        oldImages: List<String>,
        newImages: List<Uri>
    ) {
        val postRequestBody = createPostUpdateRequestBody(keywords, title, content, oldImages)

        val imagesMultipart = mutableListOf<MultipartBody.Part>()
        if (newImages.isNotEmpty()) {
            newImages.forEach { uri ->
                val imagePath = PhotoUtils.optimizeBitmap(context, uri)
                PhotoUtils.createImagesMultipart(imagePath)?.let {
                    imagesMultipart.add(it)
                }
            }
        }

        viewModelScope.launch {
            _updateResponse.value = updatePostUseCase.execute(postId, postRequestBody, imagesMultipart)
        }
    }

    private fun createPostUpdateRequestBody(
        keywords: List<String>,
        title: String,
        content: String,
        existingImageUrls: List<String>
    ): RequestBody {
        val postDTO = PostUpdateDTO(title, content, keywords, existingImageUrls)

        return PhotoUtils.createRequestBody(postDTO)
    }

    private fun createPostRequestBody(
        keywords: List<String>,
        title: String,
        content: String
    ): RequestBody {
        val postDTO = PostDTO(title, content, keywords)

        return PhotoUtils.createRequestBody(postDTO)
    }

}