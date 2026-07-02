package com.example.assetbookingmanagement.features.booking.ui

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.assetbookingmanagement.R
import com.example.assetbookingmanagement.features.booking.data.BookingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class BookingDetailsUiState(
    val isCancelling: Boolean = false,
    @param:StringRes val errorMessageResId: Int? = null
)

@HiltViewModel
class BookingDetailsViewModel @Inject constructor(
    private val bookingRepository: BookingRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(BookingDetailsUiState())
    val uiState: StateFlow<BookingDetailsUiState> = _uiState.asStateFlow()

    fun cancelBooking(
        bookingId: Long,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(isCancelling = true, errorMessageResId = null) }

            try {
                bookingRepository.cancelBooking(bookingId)
                _uiState.update { it.copy(isCancelling = false, errorMessageResId = null) }
                onSuccess()
            } catch (_: Exception) {
                _uiState.update {
                    it.copy(
                        isCancelling = false,
                        errorMessageResId = R.string.bookings_cancel_error
                    )
                }
            }
        }
    }
}
