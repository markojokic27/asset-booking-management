package com.example.assetbookingmanagement.features.user.data

import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userApi: UserApi
) {
    suspend fun getUserById(id: Long): UserResponse {
        return userApi.getUserById(id)
    }
}
