package com.project.veganlife.recipe.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.recipe.domain.usecase.RecipeUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class RecipeViewmodel @Inject constructor(
    private val recipeUsecase: RecipeUsecase,
) : ViewModel() {
    // 레시피 수정
    private val _recipeModifyResponse = MutableLiveData<Any>()
    private val recipeModifyResponse: LiveData<Any> get() = _recipeModifyResponse

    // 레시피 삭제
    private val _recipeDeleteResponse = MutableLiveData<Any>()
    private val recipeDeleteResponse: LiveData<Any> get() = _recipeDeleteResponse

    // 레시피 좋아요
    private val _recipeLikeResponse = MutableLiveData<Any>()
    private val recipeLikeResponse: LiveData<Any> get() = _recipeLikeResponse

    // 레시피 좋아요 취소
    private val _recipeLikeCancelResponse = MutableLiveData<Any>()
    private val recipeLikeCancelResponse: LiveData<Any> get() = _recipeLikeCancelResponse

    fun modifyRecipe(id: Long, recipeRequetDTO: RequestBody, recipePhotoMultipart: MultipartBody.Part) {
        viewModelScope.launch {
            val response = recipeUsecase.modifyRecipe(id, recipeRequetDTO, recipePhotoMultipart)
            when(response) {
                is ApiResult.Error -> {
                    val responseDescription = response.description
                    Log.d("withDrawal Error", responseDescription)
                }

                is ApiResult.Exception -> {
                    Log.d(
                        "withDrawal Exception",
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
                    Log.d("withDrawal Error", responseDescription)
                }

                is ApiResult.Exception -> {
                    Log.d(
                        "withDrawal Exception",
                        response.e.message ?: "No message available"
                    )
                }

                is ApiResult.Success -> {
                    _recipeDeleteResponse.value = response.data.toString()
                }
            }
        }
    }

    fun likeRecipe(id: Long) {
        viewModelScope.launch {
            val response = recipeUsecase.likeRecipe(id)
            when(response) {
                is ApiResult.Error -> {
                    val responseDescription = response.description
                    Log.d("withDrawal Error", responseDescription)
                }

                is ApiResult.Exception -> {
                    Log.d(
                        "withDrawal Exception",
                        response.e.message ?: "No message available"
                    )
                }

                is ApiResult.Success -> {
                    _recipeLikeResponse.value = response.data
                }
            }
        }
    }

    fun likeCancelRecipe(id: Long) {
        viewModelScope.launch {
            val response = recipeUsecase.likeCancelRecipe(id)
            when(response) {
                is ApiResult.Error -> {
                    val responseDescription = response.description
                    Log.d("withDrawal Error", responseDescription)
                }

                is ApiResult.Exception -> {
                    Log.d(
                        "withDrawal Exception",
                        response.e.message ?: "No message available"
                    )
                }

                is ApiResult.Success -> {
                    _recipeLikeCancelResponse.value = response.data
                }
            }
        }
    }
}