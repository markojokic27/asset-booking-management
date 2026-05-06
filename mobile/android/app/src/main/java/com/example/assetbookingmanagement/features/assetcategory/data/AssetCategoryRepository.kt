package com.example.assetbookingmanagement.features.assetcategory.data

import javax.inject.Inject

class AssetCategoryRepository @Inject constructor(
    private val assetCategoryApi: AssetCategoryApi
) {
    suspend fun getAssetCategoryById(id: Long): AssetCategoryResponse {
        return assetCategoryApi.getAssetCategoryById(id)
    }

    suspend fun getAssetCategories(): List<AssetCategoryResponse> {
        return assetCategoryApi.getAssetCategories().content
    }
}
