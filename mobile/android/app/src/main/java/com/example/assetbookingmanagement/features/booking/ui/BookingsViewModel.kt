package com.example.assetbookingmanagement.features.booking.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.assetbookingmanagement.features.asset.data.AssetRepository
import com.example.assetbookingmanagement.features.auth.data.AuthSession
import com.example.assetbookingmanagement.features.booking.data.BookingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MyBookingUiModel(
    val id: Long,
    val assetName: String
)

data class BookingsUiState(
    val isLoading: Boolean = false,
    val myBookings: List<MyBookingUiModel> = emptyList(),
    val errorMessage: String? = null
)

@HiltViewModel
class BookingsViewModel @Inject constructor(
    private val bookingRepository: BookingRepository,
    private val assetRepository: AssetRepository,
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

                val bookingItems = bookings.map { booking ->
                    val assetName = try {
                        assetRepository.getAssetById(booking.assetId).name
                    } catch (_: Exception) {
                        "Unknown asset"
                    }

                    MyBookingUiModel(
                        id = booking.id,
                        assetName = assetName
                    )
                }

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        myBookings = bookingItems
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
