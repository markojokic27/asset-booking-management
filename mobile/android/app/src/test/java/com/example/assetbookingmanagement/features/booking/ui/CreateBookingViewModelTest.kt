package com.example.assetbookingmanagement.features.booking.ui

import com.example.assetbookingmanagement.R
import com.example.assetbookingmanagement.features.asset.data.AssetApi
import com.example.assetbookingmanagement.features.asset.data.AssetListResponse
import com.example.assetbookingmanagement.features.asset.data.AssetRepository
import com.example.assetbookingmanagement.features.asset.data.AssetResponse
import com.example.assetbookingmanagement.features.asset.ui.MainDispatcherRule
import com.example.assetbookingmanagement.features.assetcategory.data.AssetCategoryApi
import com.example.assetbookingmanagement.features.assetcategory.data.AssetCategoryPageResponse
import com.example.assetbookingmanagement.features.assetcategory.data.AssetCategoryRepository
import com.example.assetbookingmanagement.features.assetcategory.data.AssetCategoryResponse
import com.example.assetbookingmanagement.features.auth.data.AuthSession
import com.example.assetbookingmanagement.features.booking.data.AssetSummary
import com.example.assetbookingmanagement.features.booking.data.BookingApi
import com.example.assetbookingmanagement.features.booking.data.BookingCreateRequest
import com.example.assetbookingmanagement.features.booking.data.BookingListResponse
import com.example.assetbookingmanagement.features.booking.data.BookingRepository
import com.example.assetbookingmanagement.features.booking.data.BookingResponse
import com.example.assetbookingmanagement.features.booking.data.BookingStatusUpdateRequest
import com.example.assetbookingmanagement.features.booking.data.CategorySummary
import com.example.assetbookingmanagement.features.booking.data.RecurringBookingCreateRequest
import com.example.assetbookingmanagement.features.booking.data.TimeSlotRequest
import com.example.assetbookingmanagement.features.booking.data.UserSummary
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.YearMonth
import java.time.ZoneId
import java.time.ZoneOffset

@OptIn(ExperimentalCoroutinesApi::class)
class CreateBookingViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun testLoadBookingPeriodLoadsMeetingRoomCategoryAndHourlyAvailability() = runTest {
        val fakeAssetApi = FakeAssetApi().apply {
            assetByIdResponse = meetingRoomAsset()
        }
        val fakeCategoryApi = FakeAssetCategoryApi().apply {
            categoryByIdResponse = meetingRoomCategory()
        }
        val booking = meetingRoomBooking(
            bookingStart = "2026-07-10T09:00:00Z",
            bookingEnd = "2026-07-10T12:00:00Z",
            status = "APPROVED"
        )
        val fakeBookingApi = FakeBookingApi().apply {
            getBookingsResponse = BookingListResponse(content = listOf(booking))
        }

        val viewModel = viewModel(
            assetApi = fakeAssetApi,
            categoryApi = fakeCategoryApi,
            bookingApi = fakeBookingApi
        )

        viewModel.loadBookingPeriod(assetId = 12L)
        advanceUntilIdle()

        val bookedDateMillis = Instant.parse(booking.bookingStart)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
            .toUtcStartOfDayMillis()
        val expectedBookedHours = (Instant.parse(booking.bookingStart)
            .atZone(ZoneId.systemDefault())
            .hour until Instant.parse(booking.bookingEnd)
            .atZone(ZoneId.systemDefault())
            .hour)
            .toSet()

        assertEquals("Meeting Room 12", viewModel.uiState.value.assetName)
        assertEquals("Meeting room", viewModel.uiState.value.categoryName)
        assertEquals("HOUR", viewModel.uiState.value.bookingPeriod)
        assertEquals(false, viewModel.uiState.value.approvalRequired)
        assertEquals(AvailabilityStatus.HOUR_BOOKED, viewModel.uiState.value.availabilityByDate[bookedDateMillis])
        assertEquals(expectedBookedHours, viewModel.uiState.value.bookedHoursByDate[bookedDateMillis])
        assertEquals(1, fakeAssetApi.getAssetByIdCalls)
        assertEquals(1, fakeCategoryApi.getAssetCategoryByIdCalls)
        assertEquals(1, fakeBookingApi.getBookingsCalls)
    }

    @Test
    fun testClearsCategoryDataWhenBookingPeriodLoadFails() = runTest {
        val failingAssetApi = object : AssetApi {
            override suspend fun getAssets(page: Int, size: Int): AssetListResponse {
                error("getAssets is not used in CreateBookingViewModel tests.")
            }

            override suspend fun getAssetById(id: Long): AssetResponse {
                throw IOException("Server unreachable")
            }
        }

        val viewModel = viewModel(assetApi = failingAssetApi)

        viewModel.loadBookingPeriod(assetId = 12L)
        advanceUntilIdle()

        assertEquals("", viewModel.uiState.value.categoryName)
        assertEquals(null, viewModel.uiState.value.bookingPeriod)
        assertEquals(null, viewModel.uiState.value.approvalRequired)
        assertTrue(viewModel.uiState.value.availabilityByDate.isEmpty())
        assertTrue(viewModel.uiState.value.bookedHoursByDate.isEmpty())
        assertTrue(viewModel.uiState.value.availableRecurringDates.isEmpty())
    }

    @Test
    fun testCreateBookingShowsErrorWhenLoggedInUserIsMissing() = runTest {
        val authSession = mock(AuthSession::class.java)
        `when`(authSession.getCurrentUserId()).thenReturn(null)

        val fakeBookingApi = FakeBookingApi()
        val viewModel = viewModel(
            bookingApi = fakeBookingApi,
            authSession = authSession
        )

        viewModel.createBooking(assetId = 1L)

        assertEquals(
            R.string.common_error_missing_logged_in_user,
            viewModel.uiState.value.errorMessageRes
        )
        assertTrue(fakeBookingApi.createBookingRequests.isEmpty())
        assertTrue(fakeBookingApi.createRecurringBookingRequests.isEmpty())
    }

    @Test
    fun testCreateBookingShowsErrorWhenDayBookingIsMissingDates() = runTest {
        val viewModel = viewModel()

        viewModel.loadBookingPeriod(assetId = 1L)
        advanceUntilIdle()

        viewModel.createBooking(assetId = 1L)

        assertEquals(
            R.string.create_booking_error_select_from_date_to_date,
            viewModel.uiState.value.errorMessageRes
        )
    }

    @Test
    fun testCreateBookingShowsErrorWhenDayBookingIsMissingToDate() = runTest {
        val viewModel = viewModel()

        viewModel.loadBookingPeriod(assetId = 1L)
        advanceUntilIdle()

        val fromDateMillis = LocalDate.of(2026, 7, 14).toUtcStartOfDayMillis()
        viewModel.onFromDateSelected(fromDateMillis)
        viewModel.onToDateSelected(null)
        viewModel.createBooking(assetId = 1L)

        assertEquals(
            R.string.create_booking_error_select_from_date_to_date,
            viewModel.uiState.value.errorMessageRes
        )
    }

    @Test
    fun testCreateBookingCreatesDayBookingForLaptopAcrossSelectedDateRange() = runTest {
        val authSession = mock(AuthSession::class.java)
        `when`(authSession.getCurrentUserId()).thenReturn(USER_ID)

        val fakeBookingApi = FakeBookingApi().apply {
            createBookingResponse = dayBooking(
                assetId = 1L,
                assetName = "Hp 15",
                categoryId = 1L,
                categoryName = "Laptops",
                bookingStart = "2026-07-14T00:00:00Z",
                bookingEnd = "2026-07-16T23:59:00Z",
                status = "PENDING"
            )
        }
        val viewModel = viewModel(
            bookingApi = fakeBookingApi,
            authSession = authSession
        )

        viewModel.loadBookingPeriod(assetId = 1L)
        advanceUntilIdle()

        val fromDateMillis = LocalDate.of(2026, 7, 14).toUtcStartOfDayMillis()
        val toDateMillis = LocalDate.of(2026, 7, 16).toUtcStartOfDayMillis()
        viewModel.onFromDateSelected(fromDateMillis)
        viewModel.onToDateSelected(toDateMillis)

        viewModel.createBooking(assetId = 1L)
        advanceUntilIdle()

        val expectedStart = expectedInstant(fromDateMillis, hour = 0, minute = 0)
        val expectedEnd = expectedInstant(toDateMillis, hour = 23, minute = 59)

        assertEquals(
            listOf(
                BookingCreateRequest(
                    userId = USER_ID,
                    assetId = 1L,
                    bookingStart = expectedStart.toString(),
                    bookingEnd = expectedEnd.toString()
                )
            ),
            fakeBookingApi.createBookingRequests
        )
        assertTrue(viewModel.uiState.value.bookingCreated)
        assertEquals(expectedStart.toString(), viewModel.uiState.value.createdBookingStart)
        assertEquals(expectedEnd.toString(), viewModel.uiState.value.createdBookingEnd)
        assertEquals(null, viewModel.uiState.value.errorMessageRes)
    }

    @Test
    fun testRecurringWeekdayToggleRemovesAlreadySelectedDayAndClearsManualSelection() = runTest {
        val fakeAssetApi = FakeAssetApi().apply {
            assetByIdResponse = parkingAsset()
        }
        val fakeCategoryApi = FakeAssetCategoryApi().apply {
            categoryByIdResponse = parkingCategory()
        }
        val viewModel = viewModel(
            assetApi = fakeAssetApi,
            categoryApi = fakeCategoryApi
        )

        viewModel.loadBookingPeriod(assetId = 16L)
        advanceUntilIdle()

        val weekday = 1
        val fromDateMillis = LocalDate.of(2026, 7, 14).toUtcStartOfDayMillis()
        val toDateMillis = LocalDate.of(2026, 7, 16).toUtcStartOfDayMillis()

        viewModel.onFromDateSelected(fromDateMillis)
        viewModel.onToDateSelected(toDateMillis)
        viewModel.onStartTimeSelected(hour = 9, minute = 0)
        viewModel.onEndTimeSelected(hour = 10, minute = 0)
        viewModel.onRecurringWeekdayToggled(weekday)
        viewModel.onRecurringWeekdayToggled(weekday)

        assertTrue(viewModel.uiState.value.selectedWeekdays.isEmpty())
        assertEquals(null, viewModel.uiState.value.selectedFromDateMillis)
        assertEquals(null, viewModel.uiState.value.selectedToDateMillis)
        assertFalse(viewModel.uiState.value.hasSelectedStartTime)
        assertFalse(viewModel.uiState.value.hasSelectedEndTime)
        assertEquals(null, viewModel.uiState.value.errorMessageRes)
    }

    @Test
    fun testFromDateSelectionMovesToDateForwardWhenExistingToDateIsEarlier() = runTest {
        val viewModel = viewModel()

        viewModel.loadBookingPeriod(assetId = 1L)
        advanceUntilIdle()

        val originalFromDate = LocalDate.of(2026, 7, 14).toUtcStartOfDayMillis()
        val earlierToDate = LocalDate.of(2026, 7, 15).toUtcStartOfDayMillis()
        val laterFromDate = LocalDate.of(2026, 7, 16).toUtcStartOfDayMillis()

        viewModel.onFromDateSelected(originalFromDate)
        viewModel.onToDateSelected(earlierToDate)
        viewModel.onFromDateSelected(laterFromDate)

        assertEquals(laterFromDate, viewModel.uiState.value.selectedFromDateMillis)
        assertEquals(laterFromDate, viewModel.uiState.value.selectedToDateMillis)
    }

    @Test
    fun testFromDateSelectionKeepsToDateWhenExistingToDateIsStillValid() = runTest {
        val viewModel = viewModel()

        viewModel.loadBookingPeriod(assetId = 1L)
        advanceUntilIdle()

        val originalFromDate = LocalDate.of(2026, 7, 14).toUtcStartOfDayMillis()
        val laterToDate = LocalDate.of(2026, 7, 16).toUtcStartOfDayMillis()
        val updatedFromDate = LocalDate.of(2026, 7, 15).toUtcStartOfDayMillis()

        viewModel.onFromDateSelected(originalFromDate)
        viewModel.onToDateSelected(laterToDate)
        viewModel.onFromDateSelected(updatedFromDate)

        assertEquals(updatedFromDate, viewModel.uiState.value.selectedFromDateMillis)
        assertEquals(laterToDate, viewModel.uiState.value.selectedToDateMillis)
    }

    @Test
    fun testCreateBookingCreatesHourlyMeetingRoomBooking() = runTest {
        val authSession = mock(AuthSession::class.java)
        `when`(authSession.getCurrentUserId()).thenReturn(USER_ID)

        val fakeAssetApi = FakeAssetApi().apply {
            assetByIdResponse = meetingRoomAsset()
        }
        val fakeCategoryApi = FakeAssetCategoryApi().apply {
            categoryByIdResponse = meetingRoomCategory()
        }
        val fakeBookingApi = FakeBookingApi().apply {
            createBookingResponse = meetingRoomBooking(
                id = 99L,
                bookingStart = "2026-07-10T07:00:00Z",
                bookingEnd = "2026-07-10T09:00:00Z",
                status = "PENDING"
            )
        }

        val viewModel = viewModel(
            assetApi = fakeAssetApi,
            categoryApi = fakeCategoryApi,
            bookingApi = fakeBookingApi,
            authSession = authSession
        )

        viewModel.loadBookingPeriod(assetId = 12L)
        advanceUntilIdle()

        val dateMillis = LocalDate.of(2026, 7, 10).toUtcStartOfDayMillis()
        viewModel.onFromDateSelected(dateMillis)
        viewModel.onStartTimeSelected(hour = 9, minute = 0)
        viewModel.onEndTimeSelected(hour = 11, minute = 0)

        viewModel.createBooking(assetId = 12L)
        advanceUntilIdle()

        val expectedStart = expectedInstant(dateMillis, hour = 9, minute = 0)
        val expectedEnd = expectedInstant(dateMillis, hour = 11, minute = 0)
        val expectedRequest = BookingCreateRequest(
            userId = USER_ID,
            assetId = 12L,
            bookingStart = expectedStart.toString(),
            bookingEnd = expectedEnd.toString()
        )

        assertEquals(listOf(expectedRequest), fakeBookingApi.createBookingRequests)
        assertTrue(viewModel.uiState.value.bookingCreated)
        assertFalse(viewModel.uiState.value.isSubmitting)
        assertEquals(expectedStart.toString(), viewModel.uiState.value.createdBookingStart)
        assertEquals(expectedEnd.toString(), viewModel.uiState.value.createdBookingEnd)
        assertEquals(null, viewModel.uiState.value.errorMessageRes)
        assertEquals(2, fakeBookingApi.getBookingsCalls)
    }

    @Test
    fun testCreateBookingShowsConflictErrorWhenMeetingRoomPeriodIsTaken() = runTest {
        val authSession = mock(AuthSession::class.java)
        `when`(authSession.getCurrentUserId()).thenReturn(USER_ID)

        val fakeAssetApi = FakeAssetApi().apply {
            assetByIdResponse = meetingRoomAsset()
        }
        val fakeCategoryApi = FakeAssetCategoryApi().apply {
            categoryByIdResponse = meetingRoomCategory()
        }
        val fakeBookingApi = FakeBookingApi().apply {
            createBookingException = buildHttpException(409)
        }

        val viewModel = viewModel(
            assetApi = fakeAssetApi,
            categoryApi = fakeCategoryApi,
            bookingApi = fakeBookingApi,
            authSession = authSession
        )

        viewModel.loadBookingPeriod(assetId = 12L)
        advanceUntilIdle()

        val dateMillis = LocalDate.of(2026, 7, 10).toUtcStartOfDayMillis()
        viewModel.onFromDateSelected(dateMillis)
        viewModel.onStartTimeSelected(hour = 9, minute = 0)
        viewModel.onEndTimeSelected(hour = 11, minute = 0)

        viewModel.createBooking(assetId = 12L)
        advanceUntilIdle()

        assertFalse(viewModel.uiState.value.bookingCreated)
        assertFalse(viewModel.uiState.value.isSubmitting)
        assertEquals(R.string.create_booking_error_period_taken, viewModel.uiState.value.errorMessageRes)
    }

    @Test
    fun testShowsErrorWhenHourlyBookingIsMissingDate() = runTest {
        val fakeAssetApi = FakeAssetApi().apply {
            assetByIdResponse = meetingRoomAsset()
        }
        val fakeCategoryApi = FakeAssetCategoryApi().apply {
            categoryByIdResponse = meetingRoomCategory()
        }

        val viewModel = viewModel(
            assetApi = fakeAssetApi,
            categoryApi = fakeCategoryApi
        )

        viewModel.loadBookingPeriod(assetId = 12L)
        advanceUntilIdle()
        viewModel.onStartTimeSelected(hour = 9, minute = 0)
        viewModel.onEndTimeSelected(hour = 10, minute = 0)

        viewModel.createBooking(assetId = 12L)

        assertEquals(
            R.string.create_booking_error_select_date_from_time_to_time,
            viewModel.uiState.value.errorMessageRes
        )
    }

    @Test
    fun testCreateBookingShowsErrorWhenHourlyBookingIsMissingTimes() = runTest {
        val fakeAssetApi = FakeAssetApi().apply {
            assetByIdResponse = meetingRoomAsset()
        }
        val fakeCategoryApi = FakeAssetCategoryApi().apply {
            categoryByIdResponse = meetingRoomCategory()
        }
        val fakeBookingApi = FakeBookingApi()
        val viewModel = viewModel(
            assetApi = fakeAssetApi,
            categoryApi = fakeCategoryApi,
            bookingApi = fakeBookingApi
        )

        viewModel.loadBookingPeriod(assetId = 12L)
        advanceUntilIdle()

        viewModel.onFromDateSelected(LocalDate.of(2026, 7, 10).toUtcStartOfDayMillis())
        viewModel.createBooking(assetId = 12L)

        assertEquals(
            R.string.create_booking_error_select_from_time_to_time,
            viewModel.uiState.value.errorMessageRes
        )
        assertTrue(fakeBookingApi.createBookingRequests.isEmpty())
    }

    @Test
    fun testCreateBookingShowsErrorWhenHourlyBookingIsMissingEndTime() = runTest {
        val fakeAssetApi = FakeAssetApi().apply {
            assetByIdResponse = meetingRoomAsset()
        }
        val fakeCategoryApi = FakeAssetCategoryApi().apply {
            categoryByIdResponse = meetingRoomCategory()
        }
        val fakeBookingApi = FakeBookingApi()
        val viewModel = viewModel(
            assetApi = fakeAssetApi,
            categoryApi = fakeCategoryApi,
            bookingApi = fakeBookingApi
        )

        viewModel.loadBookingPeriod(assetId = 12L)
        advanceUntilIdle()

        viewModel.onFromDateSelected(LocalDate.of(2026, 7, 10).toUtcStartOfDayMillis())
        viewModel.onStartTimeSelected(hour = 9, minute = 0)
        viewModel.createBooking(assetId = 12L)

        assertEquals(
            R.string.create_booking_error_select_from_time_to_time,
            viewModel.uiState.value.errorMessageRes
        )
        assertTrue(fakeBookingApi.createBookingRequests.isEmpty())
    }

    @Test
    fun testCreateBookingShowsErrorWhenHourlyBookingEndIsBeforeStart() = runTest {
        val fakeAssetApi = FakeAssetApi().apply {
            assetByIdResponse = meetingRoomAsset()
        }
        val fakeCategoryApi = FakeAssetCategoryApi().apply {
            categoryByIdResponse = meetingRoomCategory()
        }
        val fakeBookingApi = FakeBookingApi()
        val viewModel = viewModel(
            assetApi = fakeAssetApi,
            categoryApi = fakeCategoryApi,
            bookingApi = fakeBookingApi
        )

        viewModel.loadBookingPeriod(assetId = 12L)
        advanceUntilIdle()

        viewModel.onFromDateSelected(LocalDate.of(2026, 7, 10).toUtcStartOfDayMillis())
        viewModel.onStartTimeSelected(hour = 11, minute = 0)
        viewModel.onEndTimeSelected(hour = 10, minute = 30)
        viewModel.createBooking(assetId = 12L)

        assertEquals(
            R.string.create_booking_error_end_time_after_start,
            viewModel.uiState.value.errorMessageRes
        )
        assertTrue(fakeBookingApi.createBookingRequests.isEmpty())
    }

    @Test
    fun testShowsNotAuthorizedErrorWhenBackendReturns401() = runTest {
        val fakeAssetApi = FakeAssetApi().apply {
            assetByIdResponse = meetingRoomAsset()
        }
        val fakeCategoryApi = FakeAssetCategoryApi().apply {
            categoryByIdResponse = meetingRoomCategory()
        }
        val fakeBookingApi = FakeBookingApi().apply {
            createBookingException = buildHttpException(401)
        }

        val viewModel = viewModel(
            assetApi = fakeAssetApi,
            categoryApi = fakeCategoryApi,
            bookingApi = fakeBookingApi
        )

        viewModel.loadBookingPeriod(assetId = 12L)
        advanceUntilIdle()
        val dateMillis = LocalDate.of(2026, 7, 10).toUtcStartOfDayMillis()
        viewModel.onFromDateSelected(dateMillis)
        viewModel.onStartTimeSelected(hour = 9, minute = 0)
        viewModel.onEndTimeSelected(hour = 11, minute = 0)

        viewModel.createBooking(assetId = 12L)
        advanceUntilIdle()

        assertFalse(viewModel.uiState.value.bookingCreated)
        assertEquals(R.string.create_booking_error_not_authorized, viewModel.uiState.value.errorMessageRes)
    }

    @Test
    fun testShowsGenericErrorWhenBackendReturnsUnexpectedCode() = runTest {
        val fakeAssetApi = FakeAssetApi().apply {
            assetByIdResponse = meetingRoomAsset()
        }
        val fakeCategoryApi = FakeAssetCategoryApi().apply {
            categoryByIdResponse = meetingRoomCategory()
        }
        val fakeBookingApi = FakeBookingApi().apply {
            createBookingException = buildHttpException(500)
        }

        val viewModel = viewModel(
            assetApi = fakeAssetApi,
            categoryApi = fakeCategoryApi,
            bookingApi = fakeBookingApi
        )

        viewModel.loadBookingPeriod(assetId = 12L)
        advanceUntilIdle()
        val dateMillis = LocalDate.of(2026, 7, 10).toUtcStartOfDayMillis()
        viewModel.onFromDateSelected(dateMillis)
        viewModel.onStartTimeSelected(hour = 9, minute = 0)
        viewModel.onEndTimeSelected(hour = 11, minute = 0)

        viewModel.createBooking(assetId = 12L)
        advanceUntilIdle()

        assertFalse(viewModel.uiState.value.bookingCreated)
        assertEquals(R.string.create_booking_error_failed, viewModel.uiState.value.errorMessageRes)
    }

    @Test
    fun testShowsServerUnreachableErrorWhenRequestFails() = runTest {
        val fakeAssetApi = FakeAssetApi().apply {
            assetByIdResponse = meetingRoomAsset()
        }
        val fakeCategoryApi = FakeAssetCategoryApi().apply {
            categoryByIdResponse = meetingRoomCategory()
        }
        val fakeBookingApi = FakeBookingApi().apply {
            createBookingException = IOException("Server unreachable")
        }

        val viewModel = viewModel(
            assetApi = fakeAssetApi,
            categoryApi = fakeCategoryApi,
            bookingApi = fakeBookingApi
        )

        viewModel.loadBookingPeriod(assetId = 12L)
        advanceUntilIdle()
        val dateMillis = LocalDate.of(2026, 7, 10).toUtcStartOfDayMillis()
        viewModel.onFromDateSelected(dateMillis)
        viewModel.onStartTimeSelected(hour = 9, minute = 0)
        viewModel.onEndTimeSelected(hour = 11, minute = 0)

        viewModel.createBooking(assetId = 12L)
        advanceUntilIdle()

        assertFalse(viewModel.uiState.value.bookingCreated)
        assertEquals(R.string.login_error_server_unreachable, viewModel.uiState.value.errorMessageRes)
    }

    @Test
    fun testCreateBookingCreatesRecurringParkingBookingForVisibleMonth() = runTest {
        val authSession = mock(AuthSession::class.java)
        `when`(authSession.getCurrentUserId()).thenReturn(USER_ID)

        val fakeAssetApi = FakeAssetApi().apply {
            assetByIdResponse = parkingAsset()
        }
        val fakeCategoryApi = FakeAssetCategoryApi().apply {
            categoryByIdResponse = parkingCategory()
        }
        val fakeBookingApi = FakeBookingApi()

        val viewModel = viewModel(
            assetApi = fakeAssetApi,
            categoryApi = fakeCategoryApi,
            bookingApi = fakeBookingApi,
            authSession = authSession
        )

        viewModel.loadBookingPeriod(assetId = 16L)
        advanceUntilIdle()

        val month = YearMonth.now().plusMonths(1)
        val weekday = month.atDay(1).dayOfWeek.value

        viewModel.onVisibleMonthChanged(month)
        viewModel.onRecurringWeekdayToggled(weekday)
        viewModel.createBooking(assetId = 16L)
        advanceUntilIdle()

        val selectedDates = viewModel.uiState.value.availableRecurringDates
        val expectedRequest = RecurringBookingCreateRequest(
            userId = USER_ID,
            assetId = 16L,
            timeSlots = selectedDates.map { date ->
                TimeSlotRequest(
                    bookingStart = date.atTime(6, 0).atZone(ZoneId.systemDefault()).toInstant().toString(),
                    bookingEnd = date.atTime(22, 0).atZone(ZoneId.systemDefault()).toInstant().toString()
                )
            }
        )

        assertTrue(selectedDates.isNotEmpty())
        assertEquals(listOf(expectedRequest), fakeBookingApi.createRecurringBookingRequests)
        assertTrue(viewModel.uiState.value.bookingCreated)
        assertFalse(viewModel.uiState.value.isSubmitting)
        assertEquals(expectedRequest.timeSlots.first().bookingStart, viewModel.uiState.value.createdBookingStart)
        assertEquals(expectedRequest.timeSlots.last().bookingEnd, viewModel.uiState.value.createdBookingEnd)
    }

    @Test
    fun testCreateBookingShowsErrorWhenRecurringSelectionHasNoAvailableDates() = runTest {
        val authSession = mock(AuthSession::class.java)
        `when`(authSession.getCurrentUserId()).thenReturn(USER_ID)

        val month = YearMonth.now().plusMonths(1)
        val weekday = month.atDay(1).dayOfWeek.value
        val fullyBookedDates = (1..month.lengthOfMonth())
            .asSequence()
            .map(month::atDay)
            .filter { it.dayOfWeek.value == weekday }
            .toList()

        val fakeAssetApi = FakeAssetApi().apply {
            assetByIdResponse = parkingAsset()
        }
        val fakeCategoryApi = FakeAssetCategoryApi().apply {
            categoryByIdResponse = parkingCategory()
        }
        val fakeBookingApi = FakeBookingApi().apply {
            getBookingsResponse = BookingListResponse(
                content = fullyBookedDates.mapIndexed { index, date ->
                    dayBooking(
                        id = index + 1L,
                        assetId = 16L,
                        assetName = "Parking Spot 16",
                        categoryId = 3L,
                        categoryName = "Parking",
                        bookingStart = date.atTime(6, 0).atZone(ZoneId.systemDefault()).toInstant().toString(),
                        bookingEnd = date.atTime(22, 0).atZone(ZoneId.systemDefault()).toInstant().toString(),
                        status = "APPROVED"
                    )
                }
            )
        }

        val viewModel = viewModel(
            assetApi = fakeAssetApi,
            categoryApi = fakeCategoryApi,
            bookingApi = fakeBookingApi,
            authSession = authSession
        )

        viewModel.loadBookingPeriod(assetId = 16L)
        advanceUntilIdle()
        viewModel.onVisibleMonthChanged(month)
        viewModel.onRecurringWeekdayToggled(weekday)

        assertTrue(viewModel.uiState.value.availableRecurringDates.isEmpty())

        viewModel.createBooking(assetId = 16L)

        assertEquals(
            R.string.create_booking_error_no_recurring_dates,
            viewModel.uiState.value.errorMessageRes
        )
        assertTrue(fakeBookingApi.createRecurringBookingRequests.isEmpty())
    }

    @Test
    fun testCreateBookingShowsServerErrorWhenRecurringParkingRequestFails() = runTest {
        val authSession = mock(AuthSession::class.java)
        `when`(authSession.getCurrentUserId()).thenReturn(USER_ID)

        val fakeAssetApi = FakeAssetApi().apply {
            assetByIdResponse = parkingAsset()
        }
        val fakeCategoryApi = FakeAssetCategoryApi().apply {
            categoryByIdResponse = parkingCategory()
        }
        val fakeBookingApi = FakeBookingApi().apply {
            createRecurringBookingException = IOException("Server unreachable")
        }

        val viewModel = viewModel(
            assetApi = fakeAssetApi,
            categoryApi = fakeCategoryApi,
            bookingApi = fakeBookingApi,
            authSession = authSession
        )

        viewModel.loadBookingPeriod(assetId = 16L)
        advanceUntilIdle()

        val month = YearMonth.now().plusMonths(1)
        val weekday = month.atDay(1).dayOfWeek.value

        viewModel.onVisibleMonthChanged(month)
        viewModel.onRecurringWeekdayToggled(weekday)
        viewModel.createBooking(assetId = 16L)
        advanceUntilIdle()

        assertFalse(viewModel.uiState.value.bookingCreated)
        assertFalse(viewModel.uiState.value.isSubmitting)
        assertEquals(R.string.login_error_server_unreachable, viewModel.uiState.value.errorMessageRes)
    }

    @Test
    fun testShowsNotAuthorizedErrorWhenRecurringBookingReturns401() = runTest {
        val authSession = mock(AuthSession::class.java)
        `when`(authSession.getCurrentUserId()).thenReturn(USER_ID)

        val fakeAssetApi = FakeAssetApi().apply {
            assetByIdResponse = parkingAsset()
        }
        val fakeCategoryApi = FakeAssetCategoryApi().apply {
            categoryByIdResponse = parkingCategory()
        }
        val fakeBookingApi = FakeBookingApi().apply {
            createRecurringBookingException = buildHttpException(401)
        }

        val viewModel = viewModel(
            assetApi = fakeAssetApi,
            categoryApi = fakeCategoryApi,
            bookingApi = fakeBookingApi,
            authSession = authSession
        )

        viewModel.loadBookingPeriod(assetId = 16L)
        advanceUntilIdle()

        val month = YearMonth.now().plusMonths(1)
        val weekday = month.atDay(1).dayOfWeek.value
        viewModel.onVisibleMonthChanged(month)
        viewModel.onRecurringWeekdayToggled(weekday)

        viewModel.createBooking(assetId = 16L)
        advanceUntilIdle()

        assertFalse(viewModel.uiState.value.bookingCreated)
        assertEquals(R.string.create_booking_error_not_authorized, viewModel.uiState.value.errorMessageRes)
    }

    @Test
    fun testShowsConflictErrorWhenRecurringBookingReturns409() = runTest {
        val authSession = mock(AuthSession::class.java)
        `when`(authSession.getCurrentUserId()).thenReturn(USER_ID)

        val fakeAssetApi = FakeAssetApi().apply {
            assetByIdResponse = parkingAsset()
        }
        val fakeCategoryApi = FakeAssetCategoryApi().apply {
            categoryByIdResponse = parkingCategory()
        }
        val fakeBookingApi = FakeBookingApi().apply {
            createRecurringBookingException = buildHttpException(409)
        }

        val viewModel = viewModel(
            assetApi = fakeAssetApi,
            categoryApi = fakeCategoryApi,
            bookingApi = fakeBookingApi,
            authSession = authSession
        )

        viewModel.loadBookingPeriod(assetId = 16L)
        advanceUntilIdle()

        val month = YearMonth.now().plusMonths(1)
        val weekday = month.atDay(1).dayOfWeek.value
        viewModel.onVisibleMonthChanged(month)
        viewModel.onRecurringWeekdayToggled(weekday)

        viewModel.createBooking(assetId = 16L)
        advanceUntilIdle()

        assertFalse(viewModel.uiState.value.bookingCreated)
        assertEquals(R.string.create_booking_error_recurring_dates_taken, viewModel.uiState.value.errorMessageRes)
    }

    @Test
    fun testShowsGenericErrorWhenRecurringBookingReturnsUnexpectedCode() = runTest {
        val authSession = mock(AuthSession::class.java)
        `when`(authSession.getCurrentUserId()).thenReturn(USER_ID)

        val fakeAssetApi = FakeAssetApi().apply {
            assetByIdResponse = parkingAsset()
        }
        val fakeCategoryApi = FakeAssetCategoryApi().apply {
            categoryByIdResponse = parkingCategory()
        }
        val fakeBookingApi = FakeBookingApi().apply {
            createRecurringBookingException = buildHttpException(500)
        }

        val viewModel = viewModel(
            assetApi = fakeAssetApi,
            categoryApi = fakeCategoryApi,
            bookingApi = fakeBookingApi,
            authSession = authSession
        )

        viewModel.loadBookingPeriod(assetId = 16L)
        advanceUntilIdle()

        val month = YearMonth.now().plusMonths(1)
        val weekday = month.atDay(1).dayOfWeek.value
        viewModel.onVisibleMonthChanged(month)
        viewModel.onRecurringWeekdayToggled(weekday)

        viewModel.createBooking(assetId = 16L)
        advanceUntilIdle()

        assertFalse(viewModel.uiState.value.bookingCreated)
        assertEquals(R.string.create_booking_error_failed, viewModel.uiState.value.errorMessageRes)
    }

    @Test
    fun testLoadBookingPeriodIgnoresRejectedBookingsInAvailability() = runTest {
        val fakeAssetApi = FakeAssetApi().apply {
            assetByIdResponse = meetingRoomAsset()
        }
        val fakeCategoryApi = FakeAssetCategoryApi().apply {
            categoryByIdResponse = meetingRoomCategory()
        }
        val fakeBookingApi = FakeBookingApi().apply {
            getBookingsResponse = BookingListResponse(
                content = listOf(
                    meetingRoomBooking(
                        bookingStart = "2026-07-10T09:00:00Z",
                        bookingEnd = "2026-07-10T12:00:00Z",
                        status = "REJECTED"
                    )
                )
            )
        }

        val viewModel = viewModel(
            assetApi = fakeAssetApi,
            categoryApi = fakeCategoryApi,
            bookingApi = fakeBookingApi
        )

        viewModel.loadBookingPeriod(assetId = 12L)
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value.availabilityByDate.isEmpty())
        assertTrue(viewModel.uiState.value.bookedHoursByDate.isEmpty())
    }

    private fun viewModel(
        assetApi: AssetApi = FakeAssetApi(),
        categoryApi: AssetCategoryApi = FakeAssetCategoryApi(),
        bookingApi: BookingApi = FakeBookingApi(),
        authSession: AuthSession = mock(AuthSession::class.java).apply {
            `when`(getCurrentUserId()).thenReturn(USER_ID)
        }
    ) = CreateBookingViewModel(
        assetRepository = AssetRepository(assetApi),
        assetCategoryRepository = AssetCategoryRepository(categoryApi),
        bookingRepository = BookingRepository(bookingApi),
        authSession = authSession
    )

    private fun meetingRoomAsset() = AssetResponse(
        id = 12L,
        name = "Meeting Room 12",
        categoryId = 2L,
        description = "Small meeting room",
        code = "MR-001",
        status = "ACTIVE",
        location = "Floor 2"
    )

    private fun hp15Asset() = AssetResponse(
        id = 1L,
        name = "Hp 15",
        categoryId = 1L,
        description = "Laptop located in room 301",
        code = "QR-LAPTOP-001",
        status = "ACTIVE",
        location = "Room 301"
    )

    private fun parkingAsset() = AssetResponse(
        id = 16L,
        name = "Parking Spot 16",
        categoryId = 3L,
        description = "VIP parking",
        code = "PARK-016",
        status = "ACTIVE",
        location = "Floor plan 2"
    )

    private fun meetingRoomCategory() = AssetCategoryResponse(
        id = 2L,
        name = "Meeting room",
        description = "All company meeting rooms",
        bookingPeriod = "HOUR",
        approval = false
    )

    private fun parkingCategory() = AssetCategoryResponse(
        id = 3L,
        name = "Parking",
        description = "All company parkings",
        bookingPeriod = "DAY",
        approval = false
    )

    private fun meetingRoomBooking(
        id: Long = 1L,
        bookingStart: String,
        bookingEnd: String,
        status: String
    ) = BookingResponse(
        id = id,
        user = UserSummary(
            id = USER_ID,
            name = "Ivan",
            surname = "Horvat",
            email = "ivan@example.com",
            role = "USER",
            managerEmail = "manager@example.com"
        ),
        asset = AssetSummary(
            id = 12L,
            name = "Meeting Room 12",
            category = CategorySummary(
                id = 2L,
                name = "Meeting room",
                bookingPeriod = "HOUR",
                approval = false
            ),
            status = "ACTIVE",
            description = "Small meeting room",
            location = "Floor 2"
        ),
        status = status,
        bookingStart = bookingStart,
        bookingEnd = bookingEnd,
        notes = null
    )

    private fun dayBooking(
        id: Long = 1L,
        assetId: Long,
        assetName: String,
        categoryId: Long,
        categoryName: String,
        bookingStart: String,
        bookingEnd: String,
        status: String
    ) = BookingResponse(
        id = id,
        user = UserSummary(
            id = USER_ID,
            name = "Ivan",
            surname = "Horvat",
            email = "ivan@example.com",
            role = "USER",
            managerEmail = "manager@example.com"
        ),
        asset = AssetSummary(
            id = assetId,
            name = assetName,
            category = CategorySummary(
                id = categoryId,
                name = categoryName,
                bookingPeriod = "DAY",
                approval = false
            ),
            status = "ACTIVE",
            description = "Description",
            location = "Floor 2"
        ),
        status = status,
        bookingStart = bookingStart,
        bookingEnd = bookingEnd,
        notes = null
    )

    private fun buildHttpException(code: Int): HttpException {
        val errorBody = "{}".toResponseBody("application/json".toMediaType())
        return HttpException(Response.error<Any>(code, errorBody))
    }

    private fun expectedInstant(
        dateMillis: Long,
        hour: Int,
        minute: Int
    ): Instant =
        Instant.ofEpochMilli(dateMillis)
            .atZone(ZoneOffset.UTC)
            .toLocalDate()
            .atTime(LocalTime.of(hour, minute))
            .atZone(ZoneId.systemDefault())
            .toInstant()

    private fun LocalDate.toUtcStartOfDayMillis(): Long =
        atStartOfDay()
            .toInstant(ZoneOffset.UTC)
            .toEpochMilli()

    private class FakeAssetApi : AssetApi {
        var assetByIdResponse: AssetResponse = AssetResponse(
            id = 1L,
            name = "Hp 15",
            categoryId = 1L,
            description = "Laptop located in room 301",
            code = "QR-LAPTOP-001",
            status = "ACTIVE",
            location = "Room 301"
        )
        var getAssetByIdCalls: Int = 0

        override suspend fun getAssets(page: Int, size: Int): AssetListResponse {
            error("getAssets is not used in CreateBookingViewModel tests.")
        }

        override suspend fun getAssetById(id: Long): AssetResponse {
            getAssetByIdCalls++
            return assetByIdResponse
        }
    }

    private class FakeAssetCategoryApi : AssetCategoryApi {
        var categoryByIdResponse: AssetCategoryResponse = AssetCategoryResponse(
            id = 1L,
            name = "Laptops",
            description = "Laptop",
            bookingPeriod = "DAY",
            approval = false
        )
        var getAssetCategoryByIdCalls: Int = 0

        override suspend fun getAssetCategoryById(id: Long): AssetCategoryResponse {
            getAssetCategoryByIdCalls++
            return categoryByIdResponse
        }

        override suspend fun getAssetCategories(): AssetCategoryPageResponse {
            error("getAssetCategories is not used in CreateBookingViewModel tests.")
        }
    }

    private class FakeBookingApi : BookingApi {
        var getBookingsResponse: BookingListResponse = BookingListResponse(content = emptyList())
        var createBookingResponse: BookingResponse = BookingResponse(
            id = 10L,
            user = UserSummary(
                id = USER_ID,
                name = "Ivan",
                surname = "Horvat",
                email = "ivan@example.com",
                role = "USER",
                managerEmail = "manager@example.com"
            ),
            asset = AssetSummary(
                id = 12L,
                name = "Meeting Room 12",
                category = CategorySummary(
                    id = 2L,
                    name = "Meeting room",
                    bookingPeriod = "HOUR",
                    approval = false
                ),
                status = "ACTIVE",
                description = "Small meeting room",
                location = "Floor 2"
            ),
            status = "PENDING",
            bookingStart = "2026-07-10T07:00:00Z",
            bookingEnd = "2026-07-10T09:00:00Z",
            notes = null
        )
        var createRecurringBookingResponse: List<BookingResponse> = emptyList()
        var createBookingException: Exception? = null
        var createRecurringBookingException: Exception? = null
        var getBookingsCalls: Int = 0
        val createBookingRequests = mutableListOf<BookingCreateRequest>()
        val createRecurringBookingRequests = mutableListOf<RecurringBookingCreateRequest>()

        override suspend fun getBookings(
            userId: Long?,
            assetId: Long?,
            status: String?,
            page: Int,
            size: Int
        ): BookingListResponse {
            getBookingsCalls++
            return getBookingsResponse
        }

        override suspend fun createBooking(request: BookingCreateRequest): BookingResponse {
            createBookingRequests += request
            createBookingException?.let { throw it }
            return createBookingResponse
        }

        override suspend fun createRecurringBooking(request: RecurringBookingCreateRequest): List<BookingResponse> {
            createRecurringBookingRequests += request
            createRecurringBookingException?.let { throw it }
            return createRecurringBookingResponse
        }

        override suspend fun approveBooking(bookingId: Long): BookingResponse {
            error("approveBooking is not used in CreateBookingViewModel tests.")
        }

        override suspend fun rejectBooking(bookingId: Long): BookingResponse {
            error("rejectBooking is not used in CreateBookingViewModel tests.")
        }

        override suspend fun updateBooking(
            bookingId: Long,
            request: BookingStatusUpdateRequest
        ): BookingResponse {
            error("updateBooking is not used in CreateBookingViewModel tests.")
        }
    }

    companion object {
        private const val USER_ID = 2L
    }
}
