package com.example.assetbookingmanagement.features.user.ui

import com.example.assetbookingmanagement.R
import com.example.assetbookingmanagement.features.auth.data.AuthApi
import com.example.assetbookingmanagement.features.auth.data.AuthRepository
import com.example.assetbookingmanagement.features.auth.data.AuthSession
import com.example.assetbookingmanagement.features.auth.data.AuthTokenStore
import com.example.assetbookingmanagement.features.auth.data.LoginRequest
import com.example.assetbookingmanagement.features.auth.data.LoginResponse
import com.example.assetbookingmanagement.features.auth.data.RefreshTokenRequest
import com.example.assetbookingmanagement.features.auth.ui.MainDispatcherRule
import com.example.assetbookingmanagement.features.department.data.DepartmentApi
import com.example.assetbookingmanagement.features.department.data.DepartmentRepository
import com.example.assetbookingmanagement.features.department.data.DepartmentResponse
import com.example.assetbookingmanagement.features.user.data.ChangePasswordRequest
import com.example.assetbookingmanagement.features.user.data.UserApi
import com.example.assetbookingmanagement.features.user.data.UserRepository
import com.example.assetbookingmanagement.features.user.data.UserResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class ProfileViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun testInitLoadsProfileAndDepartmentName() = runTest {
        val authSession = loggedInAuthSession()
        val fakeUserApi = FakeUserApi()
        val fakeDepartmentApi = FakeDepartmentApi()

        val viewModel = buildViewModel(
            authSession = authSession,
            userApi = fakeUserApi,
            departmentApi = fakeDepartmentApi
        )
        advanceUntilIdle()

        assertFalse(viewModel.uiState.value.isLoading)
        assertEquals(buildUserResponse(), viewModel.uiState.value.profile)
        assertEquals("DEVOPS", viewModel.uiState.value.departmentName)
        assertNull(viewModel.uiState.value.errorMessageResId)
        assertEquals(listOf(2L), fakeUserApi.getUserByIdRequests)
        assertEquals(listOf(1L), fakeDepartmentApi.requests)
    }

    @Test
    fun testInitShowsMissingLoggedInUserErrorWhenSessionHasNoUserId() = runTest {
        val viewModel = buildViewModel(authSession = AuthSession())
        advanceUntilIdle()

        assertFalse(viewModel.uiState.value.isLoading)
        assertEquals(R.string.common_error_missing_logged_in_user, viewModel.uiState.value.errorMessageResId)
        assertNull(viewModel.uiState.value.profile)
    }

    @Test
    fun testInitFallsBackToDepartmentIdWhenDepartmentLookupFails() = runTest {
        val fakeDepartmentApi = FakeDepartmentApi().apply {
            getDepartmentException = IOException("Department service unavailable")
        }

        val viewModel = buildViewModel(
            authSession = loggedInAuthSession(),
            departmentApi = fakeDepartmentApi
        )
        advanceUntilIdle()

        assertEquals("1", viewModel.uiState.value.departmentName)
        assertEquals(buildUserResponse(), viewModel.uiState.value.profile)
        assertNull(viewModel.uiState.value.errorMessageResId)
    }

    @Test
    fun testLogoutClearsSessionAndMarksUserAsLoggedOut() = runTest {
        val authSession = loggedInAuthSession()
        val authTokenStore = mock(AuthTokenStore::class.java)
        val viewModel = buildViewModel(
            authSession = authSession,
            authTokenStore = authTokenStore
        )
        advanceUntilIdle()

        viewModel.logout()
        advanceUntilIdle()

        assertFalse(viewModel.uiState.value.isLoggingOut)
        assertTrue(viewModel.uiState.value.isLoggedOut)
        assertNull(authSession.accessToken)
        assertNull(authSession.refreshToken)
        assertNull(authSession.getCurrentUserId())
    }

    @Test
    fun testPrepareChangePasswordClearsExistingFormState() = runTest {
        val viewModel = buildViewModel(authSession = loggedInAuthSession())
        advanceUntilIdle()

        viewModel.onCurrentPasswordChange("currentPass123")
        viewModel.onNewPasswordChange("newPass123")
        viewModel.onConfirmNewPasswordChange("confirmNewPass123")
        viewModel.changePassword()

        assertEquals(R.string.change_password_error_confirm_mismatch, viewModel.uiState.value.confirmNewPasswordErrorResId)

        viewModel.prepareChangePassword()

        assertEquals("", viewModel.uiState.value.currentPassword)
        assertEquals("", viewModel.uiState.value.newPassword)
        assertEquals("", viewModel.uiState.value.confirmNewPassword)
        assertNull(viewModel.uiState.value.currentPasswordErrorResId)
        assertNull(viewModel.uiState.value.newPasswordErrorResId)
        assertNull(viewModel.uiState.value.confirmNewPasswordErrorResId)
        assertNull(viewModel.uiState.value.changePasswordErrorMessageResId)
    }

    @Test
    fun testChangePasswordShowsValidationErrors() = runTest {
        val viewModel = buildViewModel(authSession = loggedInAuthSession())
        advanceUntilIdle()

        viewModel.onCurrentPasswordChange(" ")
        viewModel.onNewPasswordChange("short")
        viewModel.onConfirmNewPasswordChange("confirmNewPass123")
        viewModel.changePassword()

        assertFalse(viewModel.uiState.value.isChangingPassword)
        assertEquals(R.string.change_password_error_current_required, viewModel.uiState.value.currentPasswordErrorResId)
        assertEquals(R.string.change_password_error_new_too_short, viewModel.uiState.value.newPasswordErrorResId)
        assertEquals(R.string.change_password_error_confirm_mismatch, viewModel.uiState.value.confirmNewPasswordErrorResId)
    }

    @Test
    fun testChangePasswordUpdatesStateWhenRequestSucceeds() = runTest {
        val fakeUserApi = FakeUserApi()
        val viewModel = buildViewModel(
            authSession = loggedInAuthSession(),
            userApi = fakeUserApi
        )
        advanceUntilIdle()

        viewModel.onCurrentPasswordChange("currentPass123")
        viewModel.onNewPasswordChange("newPass123")
        viewModel.onConfirmNewPasswordChange("newPass123")
        viewModel.changePassword()
        advanceUntilIdle()

        assertEquals(
            listOf(
                ChangePasswordCall(
                    id = 2L,
                    request = ChangePasswordRequest(
                        currentPassword = "currentPass123",
                        newPassword = "newPass123"
                    )
                )
            ),
            fakeUserApi.changePasswordRequests
        )
        assertFalse(viewModel.uiState.value.isChangingPassword)
        assertTrue(viewModel.uiState.value.isPasswordChanged)
        assertEquals("", viewModel.uiState.value.currentPassword)
        assertEquals("", viewModel.uiState.value.newPassword)
        assertEquals("", viewModel.uiState.value.confirmNewPassword)
        assertNull(viewModel.uiState.value.changePasswordErrorMessageResId)
    }

    @Test
    fun testChangePasswordShowsIncorrectCurrentPasswordError() = runTest {
        val fakeUserApi = FakeUserApi().apply {
            changePasswordException = buildHttpException(401)
        }
        val viewModel = buildViewModel(
            authSession = loggedInAuthSession(),
            userApi = fakeUserApi
        )
        advanceUntilIdle()

        viewModel.onCurrentPasswordChange("currentPass123")
        viewModel.onNewPasswordChange("newPass123")
        viewModel.onConfirmNewPasswordChange("newPass123")
        viewModel.changePassword()
        advanceUntilIdle()

        assertFalse(viewModel.uiState.value.isChangingPassword)
        assertFalse(viewModel.uiState.value.isPasswordChanged)
        assertEquals(R.string.change_password_error_current_incorrect, viewModel.uiState.value.changePasswordErrorMessageResId)
    }

    @Test
    fun testChangePasswordShowsBackendErrorWhenServerCannotBeReached() = runTest {
        val fakeUserApi = FakeUserApi().apply {
            changePasswordException = IOException("Server unreachable")
        }
        val viewModel = buildViewModel(
            authSession = loggedInAuthSession(),
            userApi = fakeUserApi
        )
        advanceUntilIdle()

        viewModel.onCurrentPasswordChange("currentPass123")
        viewModel.onNewPasswordChange("newPass123")
        viewModel.onConfirmNewPasswordChange("newPass123")
        viewModel.changePassword()
        advanceUntilIdle()

        assertFalse(viewModel.uiState.value.isChangingPassword)
        assertEquals(R.string.login_error_server_unreachable, viewModel.uiState.value.changePasswordErrorMessageResId)
    }

    private fun buildViewModel(
        authSession: AuthSession = loggedInAuthSession(),
        userApi: FakeUserApi = FakeUserApi(),
        departmentApi: FakeDepartmentApi = FakeDepartmentApi(),
        authTokenStore: AuthTokenStore = mock(AuthTokenStore::class.java)
    ) = ProfileViewModel(
        userRepository = UserRepository(userApi),
        departmentRepository = DepartmentRepository(departmentApi),
        authSession = authSession,
        authRepository = AuthRepository(
            authApi = FakeAuthApi(),
            authSession = authSession,
            authTokenStore = authTokenStore
        )
    )

    private fun loggedInAuthSession() = AuthSession().apply {
        setPrivateField("accessToken", "header.eyJ1c2VySWQiOiIyIn0.signature")
        setPrivateField("refreshToken", "refresh-token-2")
        setPrivateField("storedUserId", 2L)
    }

    private fun AuthSession.setPrivateField(name: String, value: Any?) {
        val field = AuthSession::class.java.getDeclaredField(name)
        field.isAccessible = true
        field.set(this, value)
    }

    private fun buildUserResponse() = UserResponse(
        id = 2L,
        username = "ivan.horvat",
        surname = "Horvat",
        name = "Ivan",
        email = "ivan@example.com",
        role = "USER",
        status = "ACTIVE",
        departmentId = 1L,
        managerEmail = "manager@example.com",
        notes = null,
        benefit = null
    )

    private fun buildHttpException(code: Int): HttpException {
        val errorBody = "{}".toResponseBody("application/json".toMediaType())
        return HttpException(Response.error<Any>(code, errorBody))
    }

    private data class ChangePasswordCall(
        val id: Long,
        val request: ChangePasswordRequest
    )

    private class FakeUserApi : UserApi {
        var userResponse: UserResponse = UserResponse(
            id = 2L,
            username = "ivan.horvat",
            surname = "Horvat",
            name = "Ivan",
            email = "ivan@example.com",
            role = "USER",
            status = "ACTIVE",
            departmentId = 1L,
            managerEmail = "manager@example.com",
            notes = null,
            benefit = null
        )
        var getUserByIdException: Exception? = null
        var changePasswordException: Exception? = null
        val getUserByIdRequests = mutableListOf<Long>()
        val changePasswordRequests = mutableListOf<ChangePasswordCall>()

        override suspend fun getUserById(id: Long): UserResponse {
            getUserByIdRequests += id
            getUserByIdException?.let { throw it }
            return userResponse
        }

        override suspend fun changePassword(id: Long, request: ChangePasswordRequest) {
            changePasswordRequests += ChangePasswordCall(id = id, request = request)
            changePasswordException?.let { throw it }
        }
    }

    private class FakeDepartmentApi : DepartmentApi {
        var departmentResponse: DepartmentResponse = DepartmentResponse(name = "DEVOPS")
        var getDepartmentException: Exception? = null
        val requests = mutableListOf<Long>()

        override suspend fun getDepartmentById(id: Long): DepartmentResponse {
            requests += id
            getDepartmentException?.let { throw it }
            return departmentResponse
        }
    }

    private class FakeAuthApi : AuthApi {
        override suspend fun login(request: LoginRequest): LoginResponse {
            error("Login is not used in ProfileViewModel tests.")
        }

        override suspend fun refresh(request: RefreshTokenRequest): LoginResponse {
            error("Refresh is not used in ProfileViewModel tests.")
        }
    }
}
