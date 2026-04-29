package com.example.assetbookingmanagement.features.assetcategory.data

import kotlinx.serialization.Serializable

@Serializable
data class AssetCategoryResponse(
    val id: Long,
    val name: String,
    val description: String? = null,
    val bookingPeriod: String,
    val approval: Boolean
)
