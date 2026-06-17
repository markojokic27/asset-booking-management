package com.example.assetbookingmanagement.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.assetbookingmanagement.features.auth.data.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AppUiState(
    val isLoading: Boolean = true,
    val isUserLoggedIn: Boolean = false
)

@HiltViewModel
class AppViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(AppUiState())
    val uiState: StateFlow<AppUiState> = _uiState.asStateFlow()

    init {
        restoreSession()
    }

    private fun restoreSession() {
        viewModelScope.launch {
            val isLoggedIn = authRepository.restoreSession()
            _uiState.update {
                it.copy(
                    isLoading = false,
                    isUserLoggedIn = isLoggedIn
                )
            }
        }
    }

    fun onUserLoggedOut() {
        _uiState.update {
            it.copy(isUserLoggedIn = false)
        }
    }
}
