package com.example.assetbookingmanagement.features.auth.data

import android.util.Base64
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import javax.inject.Inject
import javax.inject.Singleton

// Keeps the current login tokens in memory
@Singleton
class AuthSession @Inject constructor() {
    var accessToken: String? = null
        private set

    var refreshToken: String? = null
        private set

    private var storedUserId: Long? = null

    fun saveTokens(response: LoginResponse) {
        saveTokens(
            accessToken = response.accessToken,
            refreshToken = response.refreshToken
        )
    }

    fun saveTokens(
        accessToken: String,
        refreshToken: String
    ) {
        this.accessToken = accessToken
        this.refreshToken = refreshToken
        storedUserId = extractUserId(accessToken)
    }

    fun getCurrentRefreshToken(): String? = refreshToken

    fun getCurrentUserId(): Long? = storedUserId

    // Extracts the user ID from the JWT access token payload
    private fun extractUserId(token: String): Long? {
        return try {
            // JWT format: header.payload.signature
            val payload = token.split(".").getOrNull(1) ?: return null
            val decodedPayload = String(Base64.decode(payload, Base64.URL_SAFE or Base64.NO_WRAP))

            // Reads the user ID claim from the decoded JSON payload
            Json.parseToJsonElement(decodedPayload)
                .jsonObject["userId"]
                ?.jsonPrimitive
                ?.content
                ?.toLongOrNull()
        } catch (_: Exception) {
            null
        }
    }

    fun clear() {
        accessToken = null
        refreshToken = null
        storedUserId = null
    }
}
