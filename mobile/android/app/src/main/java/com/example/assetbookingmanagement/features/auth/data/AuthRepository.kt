package com.example.assetbookingmanagement.features.auth.data

import javax.inject.Inject

// Handles login data logic between the ViewModel and the backend API
class AuthRepository @Inject constructor(
    private val authApi: AuthApi,
    private val authSession: AuthSession
) {
    suspend fun login(username: String, password: String) {
        val response = authApi.login(
            LoginRequest(
                username = username.trim(),
                password = password
            )
        )

        authSession.saveTokens(response)
    }
}
