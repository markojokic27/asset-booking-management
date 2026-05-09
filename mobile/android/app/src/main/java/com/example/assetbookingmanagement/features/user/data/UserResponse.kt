package com.example.assetbookingmanagement.features.user.data

import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    val id: Long,
    val username: String,
    val surname: String,
    val name: String,
    val email: String,
    val role: String,
    val status: String,
    val departmentId: Long,
    val managerEmail: String,
    val notes: String? = null,
    val benefit: String? = null
)
