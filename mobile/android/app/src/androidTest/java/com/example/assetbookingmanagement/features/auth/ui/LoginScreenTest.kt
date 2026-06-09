package com.example.assetbookingmanagement.features.auth.ui

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.assetbookingmanagement.core.ui.theme.AssetBookingManagementTheme
import com.example.assetbookingmanagement.features.auth.data.AuthApi
import com.example.assetbookingmanagement.features.auth.data.AuthRepository
import com.example.assetbookingmanagement.features.auth.data.AuthSession
import com.example.assetbookingmanagement.features.auth.data.AuthTokenStore
import com.example.assetbookingmanagement.features.auth.data.LoginRequest
import com.example.assetbookingmanagement.features.auth.data.LoginResponse
import com.example.assetbookingmanagement.features.auth.data.RefreshTokenRequest
import kotlinx.coroutines.CompletableDeferred
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginScreenTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var fakeAuthApi: FakeAuthApi

    private lateinit var viewModel: LoginViewModel

    @Before
    fun setUp() {
        fakeAuthApi = FakeAuthApi()
        viewModel = LoginViewModel(
            AuthRepository(
                authApi = fakeAuthApi,
                authSession = AuthSession(),
                authTokenStore = AuthTokenStore(composeRule.activity)
            )
        )
    }

    @Test
    fun testShowsLoginForm() {
        setLoginScreen()

        composeRule.onNodeWithText("Login").assertIsDisplayed()
        composeRule.onNodeWithText("Username").assertIsDisplayed()
        composeRule.onNodeWithText("Password").assertIsDisplayed()
        composeRule.onNodeWithText("LOGIN").assertIsDisplayed().assertIsEnabled()
    }

    @Test
    fun testShowsValidationErrorWhenFieldsAreBlank() {
        setLoginScreen()

        composeRule.onNodeWithText("LOGIN").performClick()

        composeRule.onNodeWithText("Username and password are required.").assertIsDisplayed()
    }

    @Test
    fun testSuccessfulLoginCallsCallback() {
        val request = buildLoginRequest()
        val response = buildLoginResponse()
        var loginSuccessCalls = 0

        fakeAuthApi.response = response

        setLoginScreen(onLoginSuccess = { loginSuccessCalls++ })

        val inputFields = composeRule.onAllNodes(hasSetTextAction())
        inputFields.assertCountEquals(2)
        inputFields[0].performTextInput(request.username)
        inputFields[1].performTextInput(request.password)
        composeRule.onNodeWithText("LOGIN").performClick()

        composeRule.waitUntil(timeoutMillis = 5_000) { loginSuccessCalls == 1 }

        assertEquals(listOf(request), fakeAuthApi.requests)
        assertEquals(1, loginSuccessCalls)
    }

    @Test
    fun testShowsLoadingStateWhileLoginIsInProgress() {
        val request = buildLoginRequest()
        val response = buildLoginResponse()
        val pendingResponse = CompletableDeferred<LoginResponse>()

        fakeAuthApi.pendingResponse = pendingResponse

        setLoginScreen()

        val inputFields = composeRule.onAllNodes(hasSetTextAction())
        inputFields[0].performTextInput(request.username)
        inputFields[1].performTextInput(request.password)
        composeRule.onNodeWithText("LOGIN").performClick()

        composeRule.waitUntil(timeoutMillis = 5_000) {
            composeRule
                .onAllNodesWithText("LOGGING IN...")
                .fetchSemanticsNodes()
                .isNotEmpty()
        }

        composeRule.onNodeWithText("LOGGING IN...")
            .assertIsDisplayed()
            .assertIsNotEnabled()

        pendingResponse.complete(response)
    }

    @Test
    fun testTogglesPasswordVisibility() {
        setLoginScreen()

        composeRule.onNodeWithContentDescription("Show password").assertIsDisplayed().performClick()
        composeRule.onNodeWithContentDescription("Hide password").assertIsDisplayed()
    }

    private fun setLoginScreen(onLoginSuccess: () -> Unit = {}) {
        composeRule.setContent {
            AssetBookingManagementTheme {
                LoginScreen(
                    onLoginSuccess = onLoginSuccess,
                    viewModel = viewModel
                )
            }
        }
    }

    private fun buildLoginRequest() = LoginRequest(
        username = "ivan.horvat",
        password = "Password123!"
    )

    private fun buildLoginResponse() = LoginResponse(
        accessToken = "header.eyJ1c2VySWQiOiIxIn0.signature",
        refreshToken = "refresh-token-123"
    )

    private class FakeAuthApi : AuthApi {
        var response: LoginResponse = LoginResponse(
            accessToken = "header.eyJ1c2VySWQiOiIxIn0.signature",
            refreshToken = "refresh-token-123"
        )
        var pendingResponse: CompletableDeferred<LoginResponse>? = null
        val requests = mutableListOf<LoginRequest>()

        override suspend fun login(request: LoginRequest): LoginResponse {
            requests += request
            return pendingResponse?.await() ?: response
        }

        override suspend fun refresh(request: RefreshTokenRequest): LoginResponse {
            error("Refresh is not used in LoginScreen tests.")
        }
    }
}
