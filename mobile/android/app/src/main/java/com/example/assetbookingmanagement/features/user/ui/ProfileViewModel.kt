package com.example.assetbookingmanagement.features.user.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.assetbookingmanagement.features.auth.data.AuthSession
import com.example.assetbookingmanagement.features.user.data.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileUiState(
    val isLoading: Boolean = false,
    val userName: String = "",
    val errorMessage: String? = null
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val authSession: AuthSession
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

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
                        userName = user.name
                    )
                }
            } catch (_: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Error loading user."
                    )
                }
            }
        }
    }
}
