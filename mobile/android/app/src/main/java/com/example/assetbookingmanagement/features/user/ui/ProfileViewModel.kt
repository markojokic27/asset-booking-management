package com.example.assetbookingmanagement.features.user.ui

import androidx.annotation.StringRes
import com.example.assetbookingmanagement.R
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.assetbookingmanagement.features.auth.data.AuthRepository
import com.example.assetbookingmanagement.features.auth.data.AuthSession
import com.example.assetbookingmanagement.features.department.data.DepartmentRepository
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
    @param:StringRes val currentPasswordErrorResId: Int? = null,
    @param:StringRes val newPasswordErrorResId: Int? = null,
    @param:StringRes val confirmNewPasswordErrorResId: Int? = null,
    @param:StringRes val changePasswordErrorMessageResId: Int? = null,
    val departmentName: String = "-",
    @param:StringRes val errorMessageResId: Int? = null
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val departmentRepository: DepartmentRepository,
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
        currentPasswordErrorResId = null,
        newPasswordErrorResId = null,
        confirmNewPasswordErrorResId = null,
        changePasswordErrorMessageResId = null
    )

    init {
        getCurrentUser()
    }

    private fun getCurrentUser() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessageResId = null) }

            val userId = authSession.getCurrentUserId() ?: run {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessageResId = R.string.common_error_missing_logged_in_user
                    )
                }
                return@launch
            }

            try {
                val user = userRepository.getUserById(userId)
                val departmentName = runCatching {
                    departmentRepository.getDepartmentById(user.departmentId).name
                }.getOrNull() ?: user.departmentId.toString()
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        profile = user,
                        departmentName = departmentName,
                        errorMessageResId = null
                    )
                }
            } catch (error: HttpException) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessageResId = when (error.code()) {
                            401, 403 -> R.string.profile_error_not_authorized
                            404 -> R.string.profile_error_not_found
                            else -> R.string.profile_error_load_message
                        }
                    )
                }
            } catch (_: IOException) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessageResId = R.string.login_error_server_unreachable
                    )
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoggingOut = true, errorMessageResId = null) }

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
                currentPasswordErrorResId = null,
                changePasswordErrorMessageResId = null
            )
        }
    }

    fun onNewPasswordChange(value: String) {
        _uiState.update {
            it.copy(
                newPassword = value,
                newPasswordErrorResId = null,
                confirmNewPasswordErrorResId = null,
                changePasswordErrorMessageResId = null
            )
        }
    }

    fun onConfirmNewPasswordChange(value: String) {
        _uiState.update {
            it.copy(
                confirmNewPassword = value,
                confirmNewPasswordErrorResId = null,
                changePasswordErrorMessageResId = null
            )
        }
    }

    fun changePassword() {
        val currentState = _uiState.value
        val currentPassword = currentState.currentPassword.trim()
        val newPassword = currentState.newPassword
        val confirmNewPassword = currentState.confirmNewPassword

        val currentPasswordErrorResId = if (currentPassword.isBlank()) {
            R.string.change_password_error_current_required
        } else {
            null
        }
        val newPasswordErrorResId = when {
            newPassword.length < MIN_PASSWORD_LENGTH ->
                R.string.change_password_error_new_too_short
            newPassword.length > MAX_PASSWORD_LENGTH ->
                R.string.change_password_error_new_too_long
            else -> null
        }
        val confirmNewPasswordErrorResId = if (newPassword != confirmNewPassword) {
            R.string.change_password_error_confirm_mismatch
        } else {
            null
        }

        if (
            currentPasswordErrorResId != null ||
            newPasswordErrorResId != null ||
            confirmNewPasswordErrorResId != null
        ) {
            _uiState.update {
                it.copy(
                    currentPasswordErrorResId = currentPasswordErrorResId,
                    newPasswordErrorResId = newPasswordErrorResId,
                    confirmNewPasswordErrorResId = confirmNewPasswordErrorResId
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isChangingPassword = true,
                    currentPasswordErrorResId = null,
                    newPasswordErrorResId = null,
                    confirmNewPasswordErrorResId = null,
                    changePasswordErrorMessageResId = null
                )
            }

            val userId = authSession.getCurrentUserId() ?: run {
                _uiState.update {
                    it.copy(
                        isChangingPassword = false,
                        changePasswordErrorMessageResId = R.string.common_error_missing_logged_in_user
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
                        changePasswordErrorMessageResId = when (error.code()) {
                            401 -> R.string.change_password_error_current_incorrect
                            404 -> R.string.profile_error_not_found
                            else -> R.string.change_password_error_save_failed
                        }
                    )
                }
            } catch (_: IOException) {
                _uiState.update {
                    it.copy(
                        isChangingPassword = false,
                        changePasswordErrorMessageResId = R.string.login_error_server_unreachable
                    )
                }
            }
        }
    }
}
