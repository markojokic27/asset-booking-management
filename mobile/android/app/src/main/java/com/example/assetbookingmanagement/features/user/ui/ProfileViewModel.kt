package com.example.assetbookingmanagement.features.user.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.assetbookingmanagement.features.auth.data.AuthRepository
import com.example.assetbookingmanagement.features.auth.data.AuthSession
import com.example.assetbookingmanagement.features.user.data.UserResponse
import com.example.assetbookingmanagement.features.user.data.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

data class ProfileUiState(
    val isLoading: Boolean = false,
    val isLoggingOut: Boolean = false,
    val isLoggedOut: Boolean = false,
    val isChangingPassword: Boolean = false,
    val isPasswordChanged: Boolean = false,
    val profile: UserResponse? = null,
    val currentPassword: String = "",
    val newPassword: String = "",
    val confirmNewPassword: String = "",
    val currentPasswordError: String? = null,
    val newPasswordError: String? = null,
    val confirmNewPasswordError: String? = null,
    val changePasswordErrorMessage: String? = null,
    val errorMessage: String? = null
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val authSession: AuthSession,
    private val authRepository: AuthRepository
) : ViewModel() {
    companion object {
        private const val MIN_PASSWORD_LENGTH = 8
        private const val MAX_PASSWORD_LENGTH = 50
    }

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    private fun ProfileUiState.resetChangePasswordState(
        isPasswordChanged: Boolean = false
    ): ProfileUiState = copy(
        isChangingPassword = false,
        isPasswordChanged = isPasswordChanged,
        currentPassword = "",
        newPassword = "",
        confirmNewPassword = "",
        currentPasswordError = null,
        newPasswordError = null,
        confirmNewPasswordError = null,
        changePasswordErrorMessage = null
    )

    init {
        getCurrentUser()
    }

    private fun getCurrentUser() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val userId = authSession.getCurrentUserId() ?: run {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Missing logged in user."
                    )
                }
                return@launch
            }

            try {
                val user = userRepository.getUserById(userId)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        profile = user
                    )
                }
            } catch (error: HttpException) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = when (error.code()) {
                            401, 403 -> "You aren't authorized to view this user."
                            404 -> "User not found."
                            else -> "Failed to load user."
                        }
                    )
                }
            } catch (_: IOException) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Cannot reach backend."
                    )
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoggingOut = true, errorMessage = null) }

            authRepository.logout()

            _uiState.update {
                it.copy(
                    isLoggingOut = false,
                    isLoggedOut = true
                )
            }
        }
    }

    fun prepareChangePassword() {
        _uiState.update { it.resetChangePasswordState() }
    }

    fun clearChangePasswordState() {
        _uiState.update { it.resetChangePasswordState() }
    }

    fun onCurrentPasswordChange(value: String) {
        _uiState.update {
            it.copy(
                currentPassword = value,
                currentPasswordError = null,
                changePasswordErrorMessage = null
            )
        }
    }

    fun onNewPasswordChange(value: String) {
        _uiState.update {
            it.copy(
                newPassword = value,
                newPasswordError = null,
                confirmNewPasswordError = null,
                changePasswordErrorMessage = null
            )
        }
    }

    fun onConfirmNewPasswordChange(value: String) {
        _uiState.update {
            it.copy(
                confirmNewPassword = value,
                confirmNewPasswordError = null,
                changePasswordErrorMessage = null
            )
        }
    }

    fun changePassword() {
        val currentState = _uiState.value
        val currentPassword = currentState.currentPassword.trim()
        val newPassword = currentState.newPassword
        val confirmNewPassword = currentState.confirmNewPassword

        val currentPasswordError = if (currentPassword.isBlank()) {
            "Current password is required."
        } else {
            null
        }
        val newPasswordError = when {
            newPassword.length < MIN_PASSWORD_LENGTH ->
                "New password must be at least $MIN_PASSWORD_LENGTH characters."
            newPassword.length > MAX_PASSWORD_LENGTH ->
                "New password must be at most $MAX_PASSWORD_LENGTH characters."
            else -> null
        }
        val confirmNewPasswordError = if (newPassword != confirmNewPassword) {
            "New passwords do not match."
        } else {
            null
        }

        if (
            currentPasswordError != null ||
            newPasswordError != null ||
            confirmNewPasswordError != null
        ) {
            _uiState.update {
                it.copy(
                    currentPasswordError = currentPasswordError,
                    newPasswordError = newPasswordError,
                    confirmNewPasswordError = confirmNewPasswordError
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isChangingPassword = true,
                    currentPasswordError = null,
                    newPasswordError = null,
                    confirmNewPasswordError = null,
                    changePasswordErrorMessage = null
                )
            }

            val userId = authSession.getCurrentUserId() ?: run {
                _uiState.update {
                    it.copy(
                        isChangingPassword = false,
                        changePasswordErrorMessage = "Missing logged in user."
                    )
                }
                return@launch
            }

            try {
                userRepository.changePassword(
                    id = userId,
                    currentPassword = currentPassword,
                    newPassword = newPassword
                )
                _uiState.update { it.resetChangePasswordState(isPasswordChanged = true) }
            } catch (error: HttpException) {
                _uiState.update {
                    it.copy(
                        isChangingPassword = false,
                        changePasswordErrorMessage = when (error.code()) {
                            401 -> "Current password is incorrect."
                            404 -> "User not found."
                            else -> "Failed to change password."
                        }
                    )
                }
            } catch (_: IOException) {
                _uiState.update {
                    it.copy(
                        isChangingPassword = false,
                        changePasswordErrorMessage = "Cannot reach backend."
                    )
                }
            }
        }
    }
}
