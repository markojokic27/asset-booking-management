package com.example.assetbookingmanagement.features.asset.data

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface AssetApi {

    @GET("assets")
    suspend fun getAssets(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20
    ): AssetListResponse

    @GET("assets/{id}")
    suspend fun getAssetById(@Path("id") id: Long): AssetResponse
}