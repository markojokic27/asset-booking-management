package com.example.assetbookingmanagement.features.asset.data

import javax.inject.Inject

class AssetRepository @Inject constructor(
    private val assetApi: AssetApi
) {
    suspend fun getAssets(): AssetListResponse {
        val pageSize = 100
        var page = 0
        val all = mutableListOf<AssetResponse>()

        while (true) {
            val response = assetApi.getAssets(page = page, size = pageSize)
            all += response.content
            if (response.content.size < pageSize) break
            page++
        }

        return AssetListResponse(content = all)
    }
    suspend fun getAssetById(id: Long): AssetResponse {
        return assetApi.getAssetById(id)
    }
}