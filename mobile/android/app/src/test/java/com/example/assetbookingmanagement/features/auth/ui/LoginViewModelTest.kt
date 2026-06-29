package com.example.assetbookingmanagement.features.auth.ui

import com.example.assetbookingmanagement.R
import com.example.assetbookingmanagement.features.auth.data.AuthApi
import com.example.assetbookingmanagement.features.auth.data.AuthRepository
import com.example.assetbookingmanagement.features.auth.data.AuthSession
import com.example.assetbookingmanagement.features.auth.data.AuthTokenStore
import com.example.assetbookingmanagement.features.auth.data.LoginRequest
import com.example.assetbookingmanagement.features.auth.data.LoginResponse
import com.example.assetbookingmanagement.features.auth.data.RefreshTokenRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class LoginViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    lateinit var authTokenStore: AuthTokenStore

    @Test
    fun testLoginShowsValidationErrorWhenFieldsAreBlank() {
        val viewModel = LoginViewModel(buildAuthRepository())

        viewModel.login(username = "", password = "")

        assertEquals(
            R.string.login_error_required,
            viewModel.uiState.value.errorMessageRes
        )
        assertFalse(viewModel.uiState.value.isLoading)
        assertFalse(viewModel.uiState.value.isLoggedIn)
    }

    @Test
    fun testLoginUpdatesStateWhenLoginSucceeds() = runTest {
        val username = "ivan.horvat"
        val password = "password123"
        val fakeAuthApi = FakeAuthApi().apply {
            response = LoginResponse(
                accessToken = "header.eyJ1c2VySWQiOiIxIn0.signature",
                refreshToken = "refresh-token-123"
            )
        }
        val viewModel = LoginViewModel(buildAuthRepository(fakeAuthApi))

        viewModel.login(username, password)
        advanceUntilIdle()

        assertEquals(listOf(LoginRequest(username, password)), fakeAuthApi.loginRequests)
        assertFalse(viewModel.uiState.value.isLoading)
        assertEquals(true, viewModel.uiState.value.isLoggedIn)
        assertNull(viewModel.uiState.value.errorMessageRes)
    }

    @Test
    fun testLoginShowsUnauthorizedErrorWhenCredentialsAreWrong() = runTest {
        val username = "ivan.horvat"
        val password = "wrongPassword123"
        val fakeAuthApi = FakeAuthApi().apply {
            loginException = buildHttpException(401)
        }
        val viewModel = LoginViewModel(buildAuthRepository(fakeAuthApi))

        viewModel.login(username, password)
        advanceUntilIdle()

        assertEquals(listOf(LoginRequest(username, password)), fakeAuthApi.loginRequests)
        assertFalse(viewModel.uiState.value.isLoading)
        assertFalse(viewModel.uiState.value.isLoggedIn)
        assertEquals(R.string.login_error_invalid_credentials, viewModel.uiState.value.errorMessageRes)
    }

    @Test
    fun testLoginShowsGenericErrorWhenRequestFailsWithUnexpectedStatus() = runTest {
        val username = "ivan.horvat"
        val password = "password123"
        val fakeAuthApi = FakeAuthApi().apply {
            loginException = buildHttpException(500)
        }
        val viewModel = LoginViewModel(buildAuthRepository(fakeAuthApi))

        viewModel.login(username, password)
        advanceUntilIdle()

        assertEquals(listOf(LoginRequest(username, password)), fakeAuthApi.loginRequests)
        assertFalse(viewModel.uiState.value.isLoading)
        assertFalse(viewModel.uiState.value.isLoggedIn)
        assertEquals(R.string.login_error_server, viewModel.uiState.value.errorMessageRes)
    }

    @Test
    fun testLoginShowsBackendErrorWhenServerCannotBeReached() = runTest {
        val username = "ivan.horvat"
        val password = "password123"
        val fakeAuthApi = FakeAuthApi().apply {
            loginException = IOException("Network error")
        }
        val viewModel = LoginViewModel(buildAuthRepository(fakeAuthApi))

        viewModel.login(username, password)
        advanceUntilIdle()

        assertEquals(listOf(LoginRequest(username, password)), fakeAuthApi.loginRequests)
        assertFalse(viewModel.uiState.value.isLoading)
        assertFalse(viewModel.uiState.value.isLoggedIn)
        assertEquals(R.string.login_error_server_unreachable, viewModel.uiState.value.errorMessageRes)
    }

    private fun buildAuthRepository(
        authApi: FakeAuthApi = FakeAuthApi()
    ) = AuthRepository(
        authApi = authApi,
        authSession = AuthSession(),
        authTokenStore = authTokenStore
    )

    private fun buildHttpException(code: Int): HttpException {
        val errorBody = "{}".toResponseBody("application/json".toMediaType())
        return HttpException(Response.error<Any>(code, errorBody))
    }

    private class FakeAuthApi : AuthApi {
        var response: LoginResponse = LoginResponse(
            accessToken = "header.eyJ1c2VySWQiOiIxIn0.signature",
            refreshToken = "refresh-token-123"
        )
        var loginException: Exception? = null
        val loginRequests = mutableListOf<LoginRequest>()

        override suspend fun login(request: LoginRequest): LoginResponse {
            loginRequests += request
            loginException?.let { throw it }
            return response
        }

        override suspend fun refresh(request: RefreshTokenRequest): LoginResponse {
            error("Refresh is not used in LoginViewModel tests.")
        }
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class MainDispatcherRule : TestWatcher() {
    private val dispatcher = StandardTestDispatcher()

    override fun starting(description: Description) {
        Dispatchers.setMain(dispatcher)
    }

    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}
