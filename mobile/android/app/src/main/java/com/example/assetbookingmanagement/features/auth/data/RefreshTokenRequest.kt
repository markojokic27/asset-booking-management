package com.example.assetbookingmanagement.features.auth.data

import kotlinx.serialization.Serializable

@Serializable
data class RefreshTokenRequest(
    val refreshToken: String
)
