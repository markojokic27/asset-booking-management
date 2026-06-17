package com.example.assetbookingmanagement.features.user.data

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path

interface UserApi {

    @GET("users/{id}")
    suspend fun getUserById(@Path("id") id: Long): UserResponse

    @PATCH("users/{id}/password")
    suspend fun changePassword(
        @Path("id") id: Long,
        @Body request: ChangePasswordRequest
    )
}
