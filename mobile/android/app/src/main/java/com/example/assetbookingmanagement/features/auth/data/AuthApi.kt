package com.example.assetbookingmanagement.features.auth.data

import retrofit2.http.Body
import retrofit2.http.POST

// Defines authentication endpoints that Retrofit calls on the backend
interface AuthApi {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @POST("auth/refresh")
    suspend fun refresh(@Body request: RefreshTokenRequest): LoginResponse
}
