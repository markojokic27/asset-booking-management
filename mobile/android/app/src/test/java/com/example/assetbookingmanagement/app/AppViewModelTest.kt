package com.example.assetbookingmanagement.app

import com.example.assetbookingmanagement.features.auth.data.AuthApi
import com.example.assetbookingmanagement.features.auth.data.AuthRepository
import com.example.assetbookingmanagement.features.auth.data.AuthSession
import com.example.assetbookingmanagement.features.auth.data.AuthTokenStore
import com.example.assetbookingmanagement.features.auth.data.LoginRequest
import com.example.assetbookingmanagement.features.auth.data.LoginResponse
import com.example.assetbookingmanagement.features.auth.data.RefreshTokenRequest
import com.example.assetbookingmanagement.features.auth.ui.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

@OptIn(ExperimentalCoroutinesApi::class)
class AppViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun testInitRestoresSessionWhenRefreshTokenExists() = runTest {
        val authTokenStore = mock(AuthTokenStore::class.java)
        `when`(authTokenStore.getRefreshToken()).thenReturn("refresh-token-ivan-123")

        val viewModel = AppViewModel(
            authRepository = buildAuthRepository(
                authTokenStore = authTokenStore,
                authApi = FakeAuthApi().apply {
                    refreshResponse = LoginResponse(
                        accessToken = "header.eyJ1c2VySWQiOiIyIn0.signature",
                        refreshToken = "refresh-token-ivan-456"
                    )
                }
            )
        )
        advanceUntilIdle()

        assertFalse(viewModel.uiState.value.isLoading)
        assertEquals(true, viewModel.uiState.value.isUserLoggedIn)
        verify(authTokenStore).getRefreshToken()
        verify(authTokenStore).saveRefreshToken("refresh-token-ivan-456")
    }

    @Test
    fun testInitMarksUserLoggedOutWhenRefreshTokenIsMissing() = runTest {
        val authTokenStore = mock(AuthTokenStore::class.java)
        `when`(authTokenStore.getRefreshToken()).thenReturn(null)

        val viewModel = AppViewModel(
            authRepository = buildAuthRepository(authTokenStore = authTokenStore)
        )
        advanceUntilIdle()

        assertEquals(AppUiState(isLoading = false, isUserLoggedIn = false), viewModel.uiState.value)
        verify(authTokenStore).getRefreshToken()
    }

    @Test
    fun testInitClearsSessionWhenRefreshFails() = runTest {
        val authTokenStore = mock(AuthTokenStore::class.java)
        `when`(authTokenStore.getRefreshToken()).thenReturn("refresh-token-ivan-123")

        val authSession = AuthSession().apply {
            saveTokens(
                accessToken = "header.eyJ1c2VySWQiOiIyIn0.signature",
                refreshToken = "refresh-token-ivan-123"
            )
        }

        val viewModel = AppViewModel(
            authRepository = buildAuthRepository(
                authApi = FakeAuthApi().apply {
                    refreshException = IllegalStateException("Refresh failed for ivan.horvat")
                },
                authSession = authSession,
                authTokenStore = authTokenStore
            )
        )
        advanceUntilIdle()

        assertEquals(AppUiState(isLoading = false, isUserLoggedIn = false), viewModel.uiState.value)
        assertNull(authSession.accessToken)
        assertNull(authSession.refreshToken)
        assertNull(authSession.getCurrentUserId())
        verify(authTokenStore).clear()
    }

    @Test
    fun testOnUserLoggedOutUpdatesUiState() = runTest {
        val authTokenStore = mock(AuthTokenStore::class.java)
        `when`(authTokenStore.getRefreshToken()).thenReturn("refresh-token-ivan-123")

        val viewModel = AppViewModel(
            authRepository = buildAuthRepository(
                authTokenStore = authTokenStore,
                authApi = FakeAuthApi().apply {
                    refreshResponse = LoginResponse(
                        accessToken = "header.eyJ1c2VySWQiOiIyIn0.signature",
                        refreshToken = "refresh-token-ivan-456"
                    )
                }
            )
        )
        advanceUntilIdle()

        viewModel.onUserLoggedOut()

        assertEquals(AppUiState(isLoading = false, isUserLoggedIn = false), viewModel.uiState.value)
    }

    private fun buildAuthRepository(
        authApi: FakeAuthApi = FakeAuthApi(),
        authSession: AuthSession = AuthSession(),
        authTokenStore: AuthTokenStore
    ) = AuthRepository(
        authApi = authApi,
        authSession = authSession,
        authTokenStore = authTokenStore
    )

    private class FakeAuthApi : AuthApi {
        var refreshResponse = LoginResponse(
            accessToken = "header.eyJ1c2VySWQiOiIyIn0.signature",
            refreshToken = "refresh-token-ivan-456"
        )
        var refreshException: Exception? = null
        val refreshRequests = mutableListOf<RefreshTokenRequest>()

        override suspend fun login(request: LoginRequest): LoginResponse {
            error("Login is not used in AppViewModel tests.")
        }

        override suspend fun refresh(request: RefreshTokenRequest): LoginResponse {
            refreshRequests += request
            refreshException?.let { throw it }
            return refreshResponse
        }
    }
}
