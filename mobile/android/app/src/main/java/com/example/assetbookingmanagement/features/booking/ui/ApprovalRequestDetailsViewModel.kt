package com.example.assetbookingmanagement.features.booking.ui

import androidx.annotation.StringRes
import com.example.assetbookingmanagement.R
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.assetbookingmanagement.features.booking.data.BookingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ApprovalRequestDetailsUiState(
    val isSubmitting: Boolean = false,
    @param:StringRes val errorMessageResId: Int? = null
)

@HiltViewModel
class ApprovalRequestDetailsViewModel @Inject constructor(
    private val bookingRepository: BookingRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ApprovalRequestDetailsUiState())
    val uiState: StateFlow<ApprovalRequestDetailsUiState> = _uiState.asStateFlow()

    fun approveBooking(
        bookingId: Long,
        onSuccess: () -> Unit
    ) {
        submitAction(
            action = { bookingRepository.approveBooking(bookingId) },
            onSuccess = onSuccess
        )
    }

    fun rejectBooking(
        bookingId: Long,
        onSuccess: () -> Unit
    ) {
        submitAction(
            action = { bookingRepository.rejectBooking(bookingId) },
            onSuccess = onSuccess
        )
    }

    private fun submitAction(
        action: suspend () -> Unit,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(isSubmitting = true, errorMessageResId = null) }

            try {
                action()
                _uiState.update { it.copy(isSubmitting = false, errorMessageResId = null) }
                onSuccess()
            } catch (_: Exception) {
                _uiState.update {
                    it.copy(
                        isSubmitting = false,
                        errorMessageResId = R.string.approvals_action_error
                    )
                }
            }
        }
    }
}
