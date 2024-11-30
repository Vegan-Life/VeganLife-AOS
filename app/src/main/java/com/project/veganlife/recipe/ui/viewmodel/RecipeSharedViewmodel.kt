package com.project.veganlife.recipe.ui.viewmodel

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.recipe.data.model.RecipeDetailContent
import com.project.veganlife.recipe.domain.usecase.RecipeUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeSharedViewmodel @Inject constructor(
    private val recipeUsecase: RecipeUsecase,
    private val sharedPreferences: SharedPreferences,
): ViewModel() {
    private val _recipeId = MutableLiveData<Long>()
    val recipeId: LiveData<Long> get() = _recipeId

    // 레시피 상세 정보 변수
    private val _recipeDetailContent = MutableLiveData<RecipeDetailContent?>()
    val recipeDetailContent: LiveData<RecipeDetailContent?> get() = _recipeDetailContent

    // 레시피 작성자와 현재 로그인 사용자 간의 비교 변수
    private val _nickname = MutableLiveData<Boolean>()
    val nickName: LiveData<Boolean> get() = _nickname


    fun getRecipeDetailContent(id: Long) {
        viewModelScope.launch {
            try {
                val response = recipeUsecase.getRecipeDetailContent(id)
                when (response) {
                    is ApiResult.Error -> {
                        val response = response.description
                        Log.d("get User Info Error", response)
                    }

                    is ApiResult.Exception -> {
                        Log.d(
                            "recommended Exception",
                            response.e.message ?: "No message available"
                        )
                    }

                    is ApiResult.Success -> {
                        setRecipeDetailContent(response.data)
                        compareRecipeNickname(response.data.author.nickname)
                    }
                }
            } catch (e: Exception) {
                Log.d("ViewModel", "viewModel error: ${e.message}")
            }
        }
    }

    // 글쓴이와 사용자 비교 함수
    fun compareRecipeNickname(recipeNickname: String) {
        setNickname(recipeNickname.contentEquals(sharedPreferences.getString("userNickname",null)))
    }

    fun setRecipeDetailContent(content: RecipeDetailContent?) {
        _recipeDetailContent.value = content
    }

    fun setNickname(isEqual: Boolean) {
        _nickname.value = isEqual
    }
}