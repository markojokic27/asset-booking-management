package com.example.assetbookingmanagement.features.booking.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.assetbookingmanagement.features.asset.data.AssetRepository
import com.example.assetbookingmanagement.features.assetcategory.data.AssetCategoryRepository
import com.example.assetbookingmanagement.features.auth.data.AuthSession
import com.example.assetbookingmanagement.features.booking.data.BookingCreateRequest
import com.example.assetbookingmanagement.features.booking.data.BookingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.time.Instant
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset
import javax.inject.Inject

data class CreateBookingUiState(
    val bookingPeriod: String? = null,
    val approvalRequired: Boolean? = null,
    val startDateMillis: Long? = null,
    val endDateMillis: Long? = null,
    val startHour: Int = 9,
    val startMinute: Int = 0,
    val endHour: Int = 10,
    val endMinute: Int = 0,
    val isSubmitting: Boolean = false,
    val bookingCreated: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class CreateBookingViewModel @Inject constructor(
    private val assetRepository: AssetRepository,
    private val assetCategoryRepository: AssetCategoryRepository,
    private val bookingRepository: BookingRepository,
    private val authSession: AuthSession
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateBookingUiState())
    val uiState: StateFlow<CreateBookingUiState> = _uiState.asStateFlow()

    fun loadBookingPeriod(assetId: Long) {
        viewModelScope.launch {
            try {
                val asset = assetRepository.getAssetById(assetId)
                val category = assetCategoryRepository.getAssetCategoryById(asset.categoryId)

                _uiState.update {
                    it.copy(
                        bookingPeriod = category.bookingPeriod,
                        approvalRequired = category.approval
                    )
                }
            } catch (_: Exception) {
                _uiState.update {
                    it.copy(
                        bookingPeriod = null,
                        approvalRequired = null
                    )
                }
            }
        }
    }

    fun onStartDateSelected(dateMillis: Long?) {
        _uiState.update { it.copy(startDateMillis = dateMillis, errorMessage = null) }
    }

    fun onEndDateSelected(dateMillis: Long?) {
        _uiState.update { it.copy(endDateMillis = dateMillis, errorMessage = null) }
    }

    fun onStartTimeSelected(hour: Int, minute: Int) {
        _uiState.update {
            it.copy(startHour = hour, startMinute = minute, errorMessage = null)
        }
    }

    fun onEndTimeSelected(hour: Int, minute: Int) {
        _uiState.update {
            it.copy(endHour = hour, endMinute = minute, errorMessage = null)
        }
    }

    fun createBooking(assetId: Long) {
        val userId = authSession.getCurrentUserId()
        if (userId == null) {
            _uiState.update { it.copy(errorMessage = "Missing logged in user.") }
            return
        }

        val state = uiState.value
        val isDayBooking = state.bookingPeriod == "DAY"
        val startInstant = toInstant(
            dateMillis = state.startDateMillis,
            hour = if (isDayBooking) 0 else state.startHour,
            minute = if (isDayBooking) 0 else state.startMinute
        )
        val endInstant = toInstant(
            dateMillis = state.endDateMillis,
            hour = if (isDayBooking) 23 else state.endHour,
            minute = if (isDayBooking) 59 else state.endMinute
        )

        when {
            startInstant == null || endInstant == null -> {
                _uiState.update {
                    it.copy(
                        errorMessage = if (isDayBooking) {
                            "Please select start and end date."
                        } else {
                            "Please select start and end date and time."
                        }
                    )
                }
                return
            }

            !endInstant.isAfter(startInstant) -> {
                _uiState.update { it.copy(errorMessage = "End time must be after start time.") }
                return
            }
        }

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isSubmitting = true,
                    bookingCreated = false,
                    errorMessage = null
                )
            }

            try {
                bookingRepository.createBooking(
                    BookingCreateRequest(
                        userId = userId,
                        assetId = assetId,
                        bookingStart = startInstant.toString(),
                        bookingEnd = endInstant.toString()
                    )
                )

                _uiState.update {
                    it.copy(
                        isSubmitting = false,
                        bookingCreated = true,
                        errorMessage = null
                    )
                }
            } catch (error: HttpException) {
                _uiState.update {
                    it.copy(
                        isSubmitting = false,
                        errorMessage = when (error.code()) {
                            401, 403 -> "You aren't authorized to create this booking."
                            409 -> "Selected booking period is already taken."
                            else -> "Booking failed. Please try again."
                        }
                    )
                }
            } catch (_: IOException) {
                _uiState.update {
                    it.copy(
                        isSubmitting = false,
                        errorMessage = "Cannot reach backend."
                    )
                }
            }
        }
    }
}

private fun toInstant(
    dateMillis: Long?,
    hour: Int,
    minute: Int
): Instant? =
    dateMillis
        ?.let(Instant::ofEpochMilli)
        ?.atZone(ZoneOffset.UTC)
        ?.toLocalDate()
        ?.atTime(LocalTime.of(hour, minute))
        ?.atZone(ZoneId.systemDefault())
        ?.toInstant()
