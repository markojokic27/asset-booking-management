package com.example.assetbookingmanagement.features.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.assetbookingmanagement.features.asset.data.AssetRepository
import com.example.assetbookingmanagement.features.auth.data.AuthSession
import com.example.assetbookingmanagement.features.booking.data.BookingRepository
import com.example.assetbookingmanagement.features.user.data.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val assetCount: Int = 0,
    val myBookingsCount: Int = 0,
    val isManager: Boolean = false,
    val pendingApprovalsCount: Int = 0
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val assetRepository: AssetRepository,
    private val bookingRepository: BookingRepository,
    private val authSession: AuthSession,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        getAssetCount()
        getMyBookingsCount()
        getCurrentUserRole()
    }

    private fun getAssetCount() {
        viewModelScope.launch {
            try {
                val response = assetRepository.getAssets()
                _uiState.update { it.copy(assetCount = response.content.size) }
            } catch (_: Exception) {
                _uiState.update { it.copy(assetCount = 0) }
            }
        }
    }

    private fun getMyBookingsCount() {
        viewModelScope.launch {
            val userId = authSession.getCurrentUserId() ?: run {
                _uiState.update { it.copy(myBookingsCount = 0) }
                return@launch
            }

            try {
                val bookings = bookingRepository.getUserBookings(userId)
                _uiState.update { it.copy(myBookingsCount = bookings.size) }
            } catch (_: Exception) {
                _uiState.update { it.copy(myBookingsCount = 0) }
            }
        }
    }

    private fun getCurrentUserRole() {
        viewModelScope.launch {
            val userId = authSession.getCurrentUserId() ?: run {
                _uiState.update {
                    it.copy(
                        isManager = false,
                        pendingApprovalsCount = 0
                    )
                }
                return@launch
            }

            try {
                val user = userRepository.getUserById(userId)
                val isManager = user.role.equals("ADMIN", ignoreCase = true) ||
                    user.role.equals("MANAGER", ignoreCase = true)

                _uiState.update { it.copy(isManager = isManager) }

                if (isManager) {
                    val pendingBookings = bookingRepository.getPendingBookings()
                    _uiState.update {
                        it.copy(pendingApprovalsCount = pendingBookings.size)
                    }
                } else {
                    _uiState.update { it.copy(pendingApprovalsCount = 0) }
                }
            } catch (_: Exception) {
                _uiState.update {
                    it.copy(
                        isManager = false,
                        pendingApprovalsCount = 0
                    )
                }
            }
        }
    }
}
