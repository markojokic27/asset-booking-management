package com.example.assetbookingmanagement.features.user.data

import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userApi: UserApi
) {
    suspend fun getUserById(id: Long): UserResponse {
        return userApi.getUserById(id)
    }

    suspend fun changePassword(
        id: Long,
        currentPassword: String,
        newPassword: String
    ) {
        userApi.changePassword(
            id = id,
            request = ChangePasswordRequest(
                currentPassword = currentPassword,
                newPassword = newPassword
            )
        )
    }
}
