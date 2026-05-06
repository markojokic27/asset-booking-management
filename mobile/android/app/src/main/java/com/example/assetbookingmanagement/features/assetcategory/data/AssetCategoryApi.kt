package com.example.assetbookingmanagement.features.assetcategory.data

import retrofit2.http.GET
import retrofit2.http.Path

interface AssetCategoryApi {

    @GET("asset-categories/{id}")
    suspend fun getAssetCategoryById(@Path("id") id: Long): AssetCategoryResponse

    @GET("asset-categories")
    suspend fun getAssetCategories(): AssetCategoryPageResponse
}
