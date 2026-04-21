package com.example.assetbookingmanagement.features.asset.data

import javax.inject.Inject

class AssetRepository @Inject constructor(
    private val assetApi: AssetApi
) {
    suspend fun getAssetById(id: Long): AssetResponse {
        return assetApi.getAssetById(id)
    }
}