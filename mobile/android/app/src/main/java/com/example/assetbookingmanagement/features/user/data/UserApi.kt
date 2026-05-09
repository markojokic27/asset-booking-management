package com.example.assetbookingmanagement.features.user.data

import retrofit2.http.GET
import retrofit2.http.Path

interface UserApi {

    @GET("users/{id}")
    suspend fun getUserById(@Path("id") id: Long): UserResponse
}
