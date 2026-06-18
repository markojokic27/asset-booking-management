package com.example.assetbookingmanagement.features.asset.data

import kotlinx.serialization.Serializable

@Serializable
data class AssetResponse(
    val id: Long,
    val name: String,
    val categoryId: Long,
    val description: String? = null,
    val code: String? = null,
    val status: String,
    val location: String
)

@Serializable
data class AssetListResponse(
    val content: List<AssetResponse>,
)