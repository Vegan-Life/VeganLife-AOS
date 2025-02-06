package com.project.veganlife.recipe.data.remote

import com.project.veganlife.data.model.PagingResponse
import com.project.veganlife.recipe.data.model.RecipeDetailContent
import com.project.veganlife.recipe.data.model.RecipeFeedContent
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
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
        @Query("vegetarianType") vegetarianType: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("sort") sort: String,
    ): Response<PagingResponse<RecipeFeedContent>>

    @GET("recipes/{id}")
    suspend fun getRecipeDetailContent(
        @Path("id") id: Long
    ): Response<RecipeDetailContent>

    @DELETE("recipes/{id}")
    suspend fun deleteRecipe(
        @Path("id") id: Long
    ): Response<Any>

    @Multipart
    @PUT("recipes/{id}")
    suspend fun modifyRecipe(
        @Path("id") id: Long,
        @Part("request") RecipeRequestDTO: RequestBody,
        @Part images: List<MultipartBody.Part>
    ): Response<Any>

    @POST("recipes/{id}/likes")
    suspend fun likeRecipe(
        @Path("id") id: Long
    ): Response<Any>

    @DELETE("recipes/{id}/likes")
    suspend fun likeCancelRecipe(
        @Path("id") id: Long
    ): Response<Any>

    @Multipart
    @POST("recipes")
    suspend fun registerRecipe(
        @Part("request") RecipeRequestDTO: RequestBody,
        @Part images: List<MultipartBody.Part?>
    ): Response<Any>
}