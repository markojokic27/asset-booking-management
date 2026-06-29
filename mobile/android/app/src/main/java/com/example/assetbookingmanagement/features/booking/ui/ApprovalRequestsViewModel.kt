package com.example.assetbookingmanagement.features.booking.ui

import androidx.annotation.StringRes
import com.example.assetbookingmanagement.R
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
    val status: String,
    val requesterName: String,
    val bookingStart: String,
    val bookingEnd: String,
    val isHourlyBooking: Boolean
)

data class ApprovalRequestsUiState(
    val isLoading: Boolean = false,
    val requests: List<ApprovalRequestUiModel> = emptyList(),
    val searchText: String = "",
    @param:StringRes val errorMessageResId: Int? = null
) {
    val filteredRequests: List<ApprovalRequestUiModel>
        get() = requests.filter { request ->
            val matchesSearch =
                request.id.toString().contains(searchText, ignoreCase = true) ||
                    request.assetName.contains(searchText, ignoreCase = true) ||
                    request.requesterName.contains(searchText, ignoreCase = true) ||
                    request.status.contains(searchText, ignoreCase = true)

            matchesSearch
        }
}

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

    fun onSearchTextChange(text: String) {
        _uiState.update { it.copy(searchText = text) }
    }

    fun loadApprovalRequests() {
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
                val isManager = user.role.equals("MANAGER", ignoreCase = true)
                val isAdmin = user.role.equals("ADMIN", ignoreCase = true)

                if (!isManager && !isAdmin) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            requests = emptyList(),
                            errorMessageResId = R.string.approvals_error_no_access
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
                        },
                        errorMessageResId = null
                    )
                }
            } catch (_: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessageResId = R.string.approvals_error_load_message
                    )
                }
            }
        }
    }
}

private fun BookingResponse.toApprovalRequestUiModel(): ApprovalRequestUiModel {
    val isHourlyBooking = asset.category.bookingPeriod.equals("HOUR", ignoreCase = true)

    return ApprovalRequestUiModel(
        id = id,
        assetName = asset.name,
        status = status,
        requesterName = "${user.name} ${user.surname}".trim(),
        bookingStart = bookingStart,
        bookingEnd = bookingEnd,
        isHourlyBooking = isHourlyBooking
    )
}
