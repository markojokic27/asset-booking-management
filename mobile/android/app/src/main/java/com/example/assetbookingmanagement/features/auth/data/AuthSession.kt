package com.example.assetbookingmanagement.features.auth.data

import javax.inject.Inject
import javax.inject.Singleton

// Keeps the current login tokens in memory
@Singleton
class AuthSession @Inject constructor() {
    var accessToken: String? = null
        private set

    var refreshToken: String? = null
        private set

    fun saveTokens(response: LoginResponse) {
        accessToken = response.accessToken
        refreshToken = response.refreshToken
    }

    fun clear() {
        accessToken = null
        refreshToken = null
    }
}
