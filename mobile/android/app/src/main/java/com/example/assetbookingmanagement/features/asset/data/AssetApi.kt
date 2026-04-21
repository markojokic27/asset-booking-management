package com.example.assetbookingmanagement.features.asset.data

import retrofit2.http.GET
import retrofit2.http.Path

interface AssetApi {
    @GET("assets/{id}")
    suspend fun getAssetById(@Path("id") id: Long): AssetResponse
}