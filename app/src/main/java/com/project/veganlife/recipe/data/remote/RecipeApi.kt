package com.project.veganlife.recipe.data.remote

import com.project.veganlife.data.model.PagingResponse
import com.project.veganlife.recipe.data.model.RecipeFeedContent
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RecipeApi {
    @GET("recipes")
    suspend fun getAllRecipeFeeds(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("sort") sort: String,
    ): Response<PagingResponse<RecipeFeedContent>>

    @GET("recipes")
    suspend fun getRecipeFeedByType(
        @Query("vegetarianType") vegetarianType:String,
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("sort") sort: String,
    ): Response<PagingResponse<RecipeFeedContent>>
}