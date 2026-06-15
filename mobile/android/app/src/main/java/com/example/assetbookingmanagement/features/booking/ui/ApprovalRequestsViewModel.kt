package com.example.assetbookingmanagement.features.booking.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.assetbookingmanagement.features.auth.data.AuthSession
import com.example.assetbookingmanagement.features.booking.data.BookingRepository
import com.example.assetbookingmanagement.features.booking.data.BookingResponse
import com.example.assetbookingmanagement.features.user.data.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ApprovalRequestUiModel(
    val id: Long,
    val assetName: String,
    val bookingPeriod: String,
    val status: String,
    val requesterName: String,
    val bookingStart: String,
    val bookingEnd: String,
    val isHourlyBooking: Boolean
)

data class ApprovalRequestsUiState(
    val isLoading: Boolean = false,
    val requests: List<ApprovalRequestUiModel> = emptyList(),
    val errorMessage: String? = null
)

@HiltViewModel
class ApprovalRequestsViewModel @Inject constructor(
    private val bookingRepository: BookingRepository,
    private val authSession: AuthSession,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ApprovalRequestsUiState())
    val uiState: StateFlow<ApprovalRequestsUiState> = _uiState.asStateFlow()

    init {
        loadApprovalRequests()
    }

    fun loadApprovalRequests() {
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
                val isAdmin = user.role.equals("ADMIN", ignoreCase = true)
                val isManager = user.role.equals("MANAGER", ignoreCase = true)

                if (!isAdmin && !isManager) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            requests = emptyList(),
                            errorMessage = "You do not have access to approval requests."
                        )
                    }
                    return@launch
                }

                val pendingBookings = bookingRepository.getPendingBookings()
                val visibleBookings = if (isAdmin) {
                    pendingBookings
                } else {
                    val currentUserEmail = user.email.trim().lowercase()
                    pendingBookings.filter { booking ->
                        booking.user.managerEmail
                            ?.trim()
                            ?.lowercase() == currentUserEmail
                    }
                }

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        requests = visibleBookings.map { booking ->
                            booking.toApprovalRequestUiModel()
                        }
                    )
                }
            } catch (_: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Error loading approval requests."
                    )
                }
            }
        }
    }
}

private fun BookingResponse.toApprovalRequestUiModel() = ApprovalRequestUiModel(
    id = id,
    assetName = asset.name,
    bookingPeriod = "${bookingStart.take(10)} - ${bookingEnd.take(10)}",
    status = status,
    requesterName = "${user.name} ${user.surname}".trim(),
    bookingStart = bookingStart,
    bookingEnd = bookingEnd,
    isHourlyBooking = asset.category.bookingPeriod.equals("HOUR", ignoreCase = true)
)
