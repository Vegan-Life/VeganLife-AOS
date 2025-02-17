package com.project.veganlife.recipe.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RecipeWriteViewModel : ViewModel() {

    private val _ingredientList: MutableLiveData<List<String>> = MutableLiveData(emptyList())
    val ingredientList: LiveData<List<String>> get() = _ingredientList

    fun setIngredientList(ingredients: List<String>) {
        Log.d("##DEBUG", "setIngredientList: $ingredients")
        _ingredientList.value = ingredients
    }


    fun removeAt(position: Int) {
        val modifiedImageUris = mutableListOf<String>()
        ingredientList.value?.let { modifiedImageUris.addAll(it) }

        modifiedImageUris.removeAt(position)
        Log.d("##DEBUG", "removeAt: $modifiedImageUris")
        _ingredientList.value = modifiedImageUris
    }

    fun updateAt(position: Int, text: String) {
        val modifiedImageUris = mutableListOf<String>()
        ingredientList.value?.let { modifiedImageUris.addAll(it) }

        modifiedImageUris[position] = text
        Log.d("##DEBUG", "updateAt: $modifiedImageUris")
        _ingredientList.value = modifiedImageUris
    }

    fun add(text: String) {
        val modifiedImageUris = mutableListOf<String>()
        ingredientList.value?.let { modifiedImageUris.addAll(it) }

        modifiedImageUris.add(text)
        Log.d("##DEBUG", "add: $modifiedImageUris")
        _ingredientList.value = modifiedImageUris
    }

}