package com.example.assetbookingmanagement.features.auth.data

import javax.inject.Inject

// Handles login data logic between the ViewModel and the backend API
class AuthRepository @Inject constructor(
    private val authApi: AuthApi,
    private val authSession: AuthSession,
    private val authTokenStore: AuthTokenStore
) {
    suspend fun login(username: String, password: String) {
        val response = authApi.login(
            LoginRequest(
                username = username.trim(),
                password = password
            )
        )

        authSession.saveTokens(response)
        authTokenStore.saveRefreshToken(response.refreshToken)
    }

    suspend fun restoreSession(): Boolean {
        val storedRefreshToken = authTokenStore.getRefreshToken() ?: return false

        return try {
            val response = authApi.refresh(
                RefreshTokenRequest(refreshToken = storedRefreshToken)
            )
            authSession.saveTokens(response)
            authTokenStore.saveRefreshToken(response.refreshToken)
            true
        } catch (_: Exception) {
            authSession.clear()
            authTokenStore.clear()
            false
        }
    }

    suspend fun logout() {
        authSession.clear()
        authTokenStore.clear()
    }
}
