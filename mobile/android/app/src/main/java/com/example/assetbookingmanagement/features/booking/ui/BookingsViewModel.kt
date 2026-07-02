package com.example.assetbookingmanagement.features.booking.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.assetbookingmanagement.R
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
    val status: String,
    val bookingStart: String,
    val bookingEnd: String,
    val categoryName: String,
    val isHourlyBooking: Boolean
)

data class BookingsUiState(
    val isLoading: Boolean = false,
    val searchText: String = "",
    val myBookings: List<MyBookingUiModel> = emptyList(),
    val historyBookings: List<MyBookingUiModel> = emptyList(),
    val errorMessageResId: Int? = null
) {
    val filteredMyBookings: List<MyBookingUiModel>
        get() = myBookings.filter { booking ->
            val matchesSearch =
                booking.id.toString().contains(searchText, ignoreCase = true) ||
                    booking.assetName.contains(searchText, ignoreCase = true) ||
                    booking.categoryName.contains(searchText, ignoreCase = true) ||
                    booking.status.contains(searchText, ignoreCase = true)

            matchesSearch
        }

    val filteredHistoryBookings: List<MyBookingUiModel>
        get() = historyBookings.filter { booking ->
            val matchesSearch =
                booking.id.toString().contains(searchText, ignoreCase = true) ||
                    booking.assetName.contains(searchText, ignoreCase = true) ||
                    booking.categoryName.contains(searchText, ignoreCase = true) ||
                    booking.status.contains(searchText, ignoreCase = true)

            matchesSearch
        }
}

@HiltViewModel
class BookingsViewModel @Inject constructor(
    private val bookingRepository: BookingRepository,
    private val authSession: AuthSession
) : ViewModel() {

    private val _uiState = MutableStateFlow(BookingsUiState())
    val uiState: StateFlow<BookingsUiState> = _uiState.asStateFlow()

    fun onSearchTextChange(text: String) {
        _uiState.update { it.copy(searchText = text) }
    }

    fun refreshBookingsData() {
        viewModelScope.launch {
            val hasExistingData =
                _uiState.value.myBookings.isNotEmpty() ||
                    _uiState.value.historyBookings.isNotEmpty()

            if (!hasExistingData) {
                _uiState.update { it.copy(isLoading = true, errorMessageResId = null) }
            } else {
                _uiState.update { it.copy(errorMessageResId = null) }
            }

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
                val bookings = bookingRepository.getUserBookings(userId)
                val now = Instant.now()

                val myBookings = bookings
                    .filter { booking ->
                        runCatching { Instant.parse(booking.bookingEnd) }
                            .getOrNull()
                            ?.isBefore(now) == false
                    }
                    .map { booking ->
                        val isHourlyBooking =
                            booking.asset.category.bookingPeriod.equals("HOUR", ignoreCase = true)

                        MyBookingUiModel(
                            id = booking.id,
                            assetName = booking.asset.name,
                            status = booking.status,
                            bookingStart = booking.bookingStart,
                            bookingEnd = booking.bookingEnd,
                            categoryName = booking.asset.category.name.ifBlank { "-" },
                            isHourlyBooking = isHourlyBooking
                        )
                    }

                val historyBookings = bookings
                    .filter { booking ->
                        runCatching { Instant.parse(booking.bookingEnd) }
                            .getOrNull()
                            ?.isBefore(now) == true
                    }
                    .map { booking ->
                        val isHourlyBooking =
                            booking.asset.category.bookingPeriod.equals("HOUR", ignoreCase = true)

                        MyBookingUiModel(
                            id = booking.id,
                            assetName = booking.asset.name,
                            status = booking.status,
                            bookingStart = booking.bookingStart,
                            bookingEnd = booking.bookingEnd,
                            categoryName = booking.asset.category.name.ifBlank { "-" },
                            isHourlyBooking = isHourlyBooking
                        )
                    }

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        myBookings = myBookings,
                        historyBookings = historyBookings,
                        errorMessageResId = null
                    )
                }
            } catch (_: Exception) {
                if (!hasExistingData) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessageResId = R.string.bookings_error_load_message
                        )
                    }
                } else {
                    _uiState.update { it.copy(isLoading = false, errorMessageResId = null) }
                }
            }
        }
    }
}
