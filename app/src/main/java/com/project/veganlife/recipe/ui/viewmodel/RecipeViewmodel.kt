package com.project.veganlife.recipe.ui.viewmodel

import android.content.Context
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.recipe.data.model.RecipeModifyRequestDTO
import com.project.veganlife.recipe.data.model.RecipeRegisterRequestDTO
import com.project.veganlife.recipe.domain.usecase.RecipeUsecase
import com.project.veganlife.utils.PhotoUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class RecipeViewmodel @Inject constructor(
    private val recipeUsecase: RecipeUsecase,
) : ViewModel() {
    // 레시피 등록
    private val _recipeRegisterResponse = MutableLiveData<Any>()
    val recipeRegisterResponse: LiveData<Any> get() = _recipeRegisterResponse

    // 레시피 수정
    private val _recipeModifyResponse = MutableLiveData<Any>()
    val recipeModifyResponse: LiveData<Any> get() = _recipeModifyResponse

    // 레시피 삭제
    private val _recipeDeleteResponse = MutableLiveData<String>()
    val recipeDeleteResponse: LiveData<String> get() = _recipeDeleteResponse

    // 레시피 좋아요
    private val _recipeLikeResponse = MutableLiveData<String>()
    val recipeLikeResponse: LiveData<String> get() = _recipeLikeResponse

    // 레시피 좋아요 취소
    private val _recipeLikeCancelResponse = MutableLiveData<String>()
    val recipeLikeCancelResponse: LiveData<String> get() = _recipeLikeCancelResponse

    @RequiresApi(Build.VERSION_CODES.R)
    fun registerRecipe(
        context: Context,
        recipeTitle: String,
        recipeType: List<String>,
        ingredientList: List<String>,
        descriptionList: List<String>,
        imagesUris: List<Uri>
    ) {
        val requetDTO = PhotoUtils.createRequestBody(
            RecipeRegisterRequestDTO(
                recipeTitle = recipeTitle,
                recipeType = recipeType,
                ingredients = ingredientList,
                descriptions = descriptionList
            )
        )

        val imagesMultipart = mutableListOf<MultipartBody.Part>()
        imagesUris.forEach { uri ->
            // 1. 최적화된 비트맵을 임시 파일로 저장
            val imagePath = PhotoUtils.optimizeBitmap(context, uri)
            PhotoUtils.createImagesMultipart(imagePath)?.let {
                // 2. 임시 파일 경로를 사용해 MultipartBody.Part로 변환
                imagesMultipart.add(it)
            }
        }

        viewModelScope.launch {
//            _recipeRegisterResponse.value = recipeUsecase.registerRecipe(requetDTO, imagesMultipart)
            val response = recipeUsecase.registerRecipe(requetDTO, imagesMultipart)
            when(response) {
                is ApiResult.Error -> {
                    val responseDescription = response.description
                    Log.d("recipe register Error", responseDescription)
                }
                is ApiResult.Exception -> {
                    Log.d(
                        "recipe register Exception",
                        response.e.message ?: "No message available"
                    )
                }
                is ApiResult.Success -> {
                    _recipeRegisterResponse.value = response.data
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    fun modifyRecipe(
        context: Context,
        id: Long,
        recipeTitle: String,
        recipeType: List<String>,
        ingredientList: List<String>,
        descriptionList: List<String>,
        existingImageUrls: List<String>,
        imagesUris: List<Uri>
    ) {
        val requetDTO = PhotoUtils.createRequestBody(
            RecipeModifyRequestDTO(
                recipeTitle = recipeTitle,
                recipeType = recipeType,
                ingredients = ingredientList,
                descriptions = descriptionList,
                existingImageUrls = existingImageUrls
            )
        )

        val imagesMultipart = mutableListOf<MultipartBody.Part>()
        imagesUris.forEach { uri ->
            // 1. 최적화된 비트맵을 임시 파일로 저장
            val imagePath = PhotoUtils.optimizeBitmap(context, uri)
            PhotoUtils.createImagesMultipart(imagePath)?.let {
                // 2. 임시 파일 경로를 사용해 MultipartBody.Part로 변환
                imagesMultipart.add(it)
            }
        }

        viewModelScope.launch {
//            _recipeModifyResponse.value = recipeUsecase.modifyRecipe(id, requetDTO, imagesMultipart)
            val response = recipeUsecase.modifyRecipe(id, requetDTO, imagesMultipart)
            when(response) {
                is ApiResult.Error -> {
                    val responseDescription = response.description
                    Log.d("recipe modify Error", responseDescription)
                }
                is ApiResult.Exception -> {
                    Log.d(
                        "recipe modify Exception",
                        response.e.message ?: "No message available"
                    )
                }
                is ApiResult.Success -> {
                    _recipeModifyResponse.value = response.data
                }
            }
        }
    }

    fun deleteRecipe(id: Long) {
        viewModelScope.launch {
            val response = recipeUsecase.deleteRecipe(id)
            when(response) {
                is ApiResult.Error -> {
                    val responseDescription = response.description
                    Log.d("recipe delete Error", responseDescription)
                }

                is ApiResult.Exception -> {
                    Log.d(
                        "recipe delete Exception",
                        response.e.message ?: "No message available"
                    )
                }

                is ApiResult.Success -> {
                    _recipeDeleteResponse.value = "레시피 삭제"
                }
            }
        }
    }

    fun likeRecipe(id: Long) {
        viewModelScope.launch {
            recipeUsecase.likeRecipe(id)
        }
    }

    fun likeCancelRecipe(id: Long) {
        viewModelScope.launch {
            recipeUsecase.likeCancelRecipe(id)
        }
    }
}