package com.example.assetbookingmanagement.features.user.data

import kotlinx.serialization.Serializable

@Serializable
data class ChangePasswordRequest(
    val currentPassword: String,
    val newPassword: String
)
