package com.example.assetbookingmanagement.features.assetcategory.data

import kotlinx.serialization.Serializable

@Serializable
data class AssetCategoryPageResponse(
    val content: List<AssetCategoryResponse>
)