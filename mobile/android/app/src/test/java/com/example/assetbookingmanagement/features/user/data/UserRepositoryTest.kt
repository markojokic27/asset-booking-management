package com.example.assetbookingmanagement.features.user.data

import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class UserRepositoryTest {

    @Mock
    lateinit var userApi: UserApi

    private lateinit var repository: UserRepository

    @Before
    fun setUp() {
        repository = UserRepository(userApi)
    }

    @Test
    fun testGetUserById() = runTest {
        val userId = 1L
        val expectedUser = buildUserResponse()

        `when`(userApi.getUserById(userId)).thenReturn(expectedUser)

        val result = repository.getUserById(userId)

        verify(userApi).getUserById(userId)
        assertEquals(expectedUser, result)
    }

    private fun buildUserResponse() = UserResponse(
        id = 1L,
        username = "ivan.horvat",
        surname = "Horvat",
        name = "Ivan",
        email = "ivan@example.com",
        role = "ADMIN",
        status = "ACTIVE",
        departmentId = 42L,
        managerEmail = "manager@example.com",
        benefit = "some benefit"
    )
}
