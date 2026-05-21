package com.example.assetbookingmanagement.features.booking.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.assetbookingmanagement.features.auth.data.AuthSession
import com.example.assetbookingmanagement.features.booking.data.BookingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject

data class MyBookingUiModel(
    val id: Long,
    val assetName: String,
    val bookingPeriod: String,
    val status: String
)

data class BookingsUiState(
    val isLoading: Boolean = false,
    val myBookings: List<MyBookingUiModel> = emptyList(),
    val historyBookings: List<MyBookingUiModel> = emptyList(),
    val errorMessage: String? = null
)

@HiltViewModel
class BookingsViewModel @Inject constructor(
    private val bookingRepository: BookingRepository,
    private val authSession: AuthSession
) : ViewModel() {

    private val _uiState = MutableStateFlow(BookingsUiState())
    val uiState: StateFlow<BookingsUiState> = _uiState.asStateFlow()

    init {
        getMyBookings()
    }

    fun getMyBookings() {
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
                val bookings = bookingRepository.getUserBookings(userId)
                val now = Instant.now()

                val myBookings = bookings
                    .filter { booking ->
                        runCatching { Instant.parse(booking.bookingEnd) }
                            .getOrNull()
                            ?.isBefore(now) == false
                    }
                    .map { booking ->
                        MyBookingUiModel(
                            id = booking.id,
                            assetName = booking.asset.name,
                            bookingPeriod = "${booking.bookingStart.take(10)} - ${booking.bookingEnd.take(10)}",
                            status = booking.status
                        )
                    }

                val historyBookings = bookings
                    .filter { booking ->
                        runCatching { Instant.parse(booking.bookingEnd) }
                            .getOrNull()
                            ?.isBefore(now) == true
                    }
                    .map { booking ->
                        MyBookingUiModel(
                            id = booking.id,
                            assetName = booking.asset.name,
                            bookingPeriod = "${booking.bookingStart.take(10)} - ${booking.bookingEnd.take(10)}",
                            status = booking.status
                        )
                    }

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        myBookings = myBookings,
                        historyBookings = historyBookings
                    )
                }
            } catch (_: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Error loading bookings."
                    )
                }
            }
        }
    }
}
