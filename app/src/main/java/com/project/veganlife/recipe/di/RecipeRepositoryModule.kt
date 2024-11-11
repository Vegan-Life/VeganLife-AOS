package com.project.veganlife.recipe.di

import com.project.veganlife.recipe.data.repositoryImpl.RecipeRepositoryImpl
import com.project.veganlife.recipe.domain.repository.RecipeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RecipeRepositoryModule {
    @Provides
    @Singleton
    fun provideRecipeRepository(recipeRepositoryImpl: RecipeRepositoryImpl): RecipeRepository{
        return recipeRepositoryImpl
    }
}