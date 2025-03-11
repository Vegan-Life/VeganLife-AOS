package com.project.veganlife.recipe.ui.viewmodel

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.recipe.data.model.RecipeDetailContent
import com.project.veganlife.recipe.data.model.RecipeFeedContent
import com.project.veganlife.recipe.domain.usecase.RecipeUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val recipeUsecase: RecipeUsecase,
    private val sharedPreferences: SharedPreferences,
) : ViewModel() {
    private val _recipeDetailContent = MutableLiveData<RecipeDetailContent>()
    val recipeDetailContent: LiveData<RecipeDetailContent> = _recipeDetailContent

    init {
        val recipeId = savedStateHandle.get<RecipeFeedContent>("recipe")?.id
        if (recipeId != null) {
            getRecipeDetailContent(recipeId)
        }
    }

    fun getValue(): String {
        return sharedPreferences.getString("userNickname", "") ?: ""
    }

    private fun getRecipeDetailContent(recipeId: Long) {
        viewModelScope.launch {
            try {
                val response = recipeUsecase.getRecipeDetailContent(recipeId)
                when (response) {
                    is ApiResult.Error -> {
                        val response = response.description
                        Log.d("get Recipe Info Error", response)
                    }

                    is ApiResult.Exception -> {
                        Log.d(
                            "Get Recipe Info Exception",
                            response.e.message ?: "No message available"
                        )
                    }

                    is ApiResult.Success -> {
                        _recipeDetailContent.value = response.data
                    }
                }
            } catch (e: Exception) {
                Log.d("ViewModel", "viewModel error: ${e.message}")
            }
        }
    }
}
