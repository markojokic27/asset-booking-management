package com.example.assetbookingmanagement.features.booking.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.assetbookingmanagement.features.asset.data.AssetRepository
import com.example.assetbookingmanagement.features.assetcategory.data.AssetCategoryRepository
import com.example.assetbookingmanagement.features.auth.data.AuthSession
import com.example.assetbookingmanagement.features.booking.data.BookingCreateRequest
import com.example.assetbookingmanagement.features.booking.data.BookingRepository
import com.example.assetbookingmanagement.features.booking.data.BookingResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset
import javax.inject.Inject

enum class AvailabilityStatus {
    DAY_BOOKED,
    HOUR_BOOKED
}

data class CreateBookingUiState(
    val assetName: String = "",
    val bookingPeriod: String? = null,
    val approvalRequired: Boolean? = null,
    val availabilityByDate: Map<Long, AvailabilityStatus> = emptyMap(),
    val bookedHoursByDate: Map<Long, Set<Int>> = emptyMap(),
    val selectedFromDateMillis: Long? = null,
    val selectedToDateMillis: Long? = null,
    val startHour: Int = 9,
    val startMinute: Int = 0,
    val endHour: Int = 10,
    val endMinute: Int = 0,
    val hasSelectedStartTime: Boolean = false,
    val hasSelectedEndTime: Boolean = false,
    val isSubmitting: Boolean = false,
    val bookingCreated: Boolean = false,
    val createdBookingStart: String? = null,
    val createdBookingEnd: String? = null,
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
                val assetCategory = assetCategoryRepository.getAssetCategoryById(asset.categoryId)

                _uiState.update {
                    it.copy(
                        assetName = asset.name,
                        bookingPeriod = assetCategory.bookingPeriod,
                        approvalRequired = assetCategory.approval
                    )
                }

                refreshAvailability(assetId, assetCategory.bookingPeriod)
            } catch (_: Exception) {
                _uiState.update {
                    it.copy(
                        bookingPeriod = null,
                        approvalRequired = null,
                        availabilityByDate = emptyMap(),
                        bookedHoursByDate = emptyMap()
                    )
                }
            }
        }
    }

    fun onFromDateSelected(dateMillis: Long?) {
        _uiState.update { state ->
            val nextToDateMillis = when {
                dateMillis == null -> state.selectedToDateMillis
                state.selectedToDateMillis == null -> dateMillis
                state.selectedToDateMillis < dateMillis -> dateMillis
                else -> state.selectedToDateMillis
            }

            state.copy(
                selectedFromDateMillis = dateMillis,
                selectedToDateMillis = nextToDateMillis,
                errorMessage = null
            )
        }
    }

    fun onToDateSelected(dateMillis: Long?) {
        _uiState.update { state ->
            val nextFromDateMillis = when {
                dateMillis == null -> state.selectedFromDateMillis
                state.selectedFromDateMillis == null -> dateMillis
                state.selectedFromDateMillis > dateMillis -> dateMillis
                else -> state.selectedFromDateMillis
            }

            state.copy(
                selectedFromDateMillis = nextFromDateMillis,
                selectedToDateMillis = dateMillis,
                errorMessage = null
            )
        }
    }

    fun onStartTimeSelected(hour: Int, minute: Int) {
        _uiState.update {
            it.copy(
                startHour = hour,
                startMinute = minute,
                hasSelectedStartTime = true,
                errorMessage = null
            )
        }
    }

    fun onEndTimeSelected(hour: Int, minute: Int) {
        _uiState.update {
            it.copy(
                endHour = hour,
                endMinute = minute,
                hasSelectedEndTime = true,
                errorMessage = null
            )
        }
    }

    fun createBooking(assetId: Long) {
        val userId = authSession.getCurrentUserId()
        if (userId == null) {
            _uiState.update { it.copy(errorMessage = "Missing logged in user.") }
            return
        }

        val state = uiState.value
        val isHourlyBooking = state.bookingPeriod == "HOUR"
        val startInstant = if (isHourlyBooking) {
            toInstant(
                dateMillis = state.selectedFromDateMillis,
                hour = state.startHour,
                minute = state.startMinute
            )
        } else {
            toInstant(
                dateMillis = state.selectedFromDateMillis,
                hour = 0,
                minute = 0
            )
        }
        val endInstant = if (isHourlyBooking) {
            toInstant(
                dateMillis = state.selectedFromDateMillis,
                hour = state.endHour,
                minute = state.endMinute
            )
        } else {
            toInstant(
                dateMillis = state.selectedToDateMillis,
                hour = 23,
                minute = 59
            )
        }

        when {
            state.selectedFromDateMillis == null -> {
                _uiState.update {
                    it.copy(
                        errorMessage = if (isHourlyBooking) {
                            "Select date, from time and to time."
                        } else {
                            "Select from date and to date."
                        }
                    )
                }
                return
            }

            !isHourlyBooking && state.selectedToDateMillis == null -> {
                _uiState.update {
                    it.copy(errorMessage = "Select from date and to date.")
                }
                return
            }

            startInstant == null || endInstant == null -> {
                _uiState.update {
                    it.copy(errorMessage = "Booking period cannot be created from the selected date.")
                }
                return
            }

            isHourlyBooking && (!state.hasSelectedStartTime || !state.hasSelectedEndTime) -> {
                _uiState.update {
                    it.copy(errorMessage = "Select from time and to time.")
                }
                return
            }

            isHourlyBooking && !endInstant.isAfter(startInstant) -> {
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

                refreshAvailability(assetId, state.bookingPeriod)

                _uiState.update {
                    it.copy(
                        isSubmitting = false,
                        bookingCreated = true,
                        createdBookingStart = startInstant.toString(),
                        createdBookingEnd = endInstant.toString(),
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
                            else -> "Booking failed. Try again."
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

    private suspend fun refreshAvailability(
        assetId: Long,
        bookingPeriod: String?
    ) {
        val blockingBookings = bookingRepository.getAssetBookings(assetId)
            .filter { it.blocksAvailability() }
        val bookedDateStatus = if (bookingPeriod == "HOUR") {
            AvailabilityStatus.HOUR_BOOKED
        } else {
            AvailabilityStatus.DAY_BOOKED
        }
        val availabilityByDate = blockingBookings
            .flatMap { booking -> booking.bookingStart.toDateMillisRange(booking.bookingEnd) }
            .associateWith { bookedDateStatus }
        val bookedHoursByDate = if (bookingPeriod == "HOUR") {
            blockingBookings
                .flatMap { booking -> booking.toBookedHoursByDate().entries }
                .groupBy({ it.key }, { it.value })
                .mapValues { (_, hourSets) -> hourSets.flatten().toSet() }
        } else {
            emptyMap()
        }

        _uiState.update {
            it.copy(
                availabilityByDate = availabilityByDate,
                bookedHoursByDate = bookedHoursByDate
            )
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

//Generates a list of UTC start-of-day timestamps for each date in the range from startDateTime to endDateTime
private fun String.toDateMillisRange(endDateTime: String): List<Long> {
    val startDate = Instant.parse(this)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()
    val endDate = Instant.parse(endDateTime)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()

    return generateSequence(startDate) { currentDate ->
        currentDate.plusDays(1).takeIf { !it.isAfter(endDate) }
    }
        .map(LocalDate::toUtcStartOfDayMillis)
        .toList()
}

private fun LocalDate.toUtcStartOfDayMillis(): Long =
    atStartOfDay()
        .toInstant(ZoneOffset.UTC)
        .toEpochMilli()

private fun BookingResponse.toBookedHoursByDate(): Map<Long, Set<Int>> {
    val startDateTime = Instant.parse(bookingStart).atZone(ZoneId.systemDefault())
    val endDateTime = Instant.parse(bookingEnd).atZone(ZoneId.systemDefault())
    if (startDateTime.toLocalDate() != endDateTime.toLocalDate()) {
        return emptyMap()
    }

    return mapOf(
        startDateTime.toLocalDate().toUtcStartOfDayMillis() to
            (startDateTime.hour until endDateTime.hour).toSet()
    )
}

private fun BookingResponse.blocksAvailability(): Boolean =
    status == "APPROVED" || status == "PENDING"
