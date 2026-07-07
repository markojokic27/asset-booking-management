package com.example.assetbookingmanagement.features.booking.ui

import com.example.assetbookingmanagement.R
import com.example.assetbookingmanagement.features.asset.ui.MainDispatcherRule
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
import com.example.assetbookingmanagement.features.booking.data.UserSummary
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import java.time.Instant
import java.time.temporal.ChronoUnit

@OptIn(ExperimentalCoroutinesApi::class)
class BookingsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun testSplitsActiveAndHistoryBookings() = runTest {
        val authSession = mock(AuthSession::class.java)
        `when`(authSession.getCurrentUserId()).thenReturn(2L)

        val fakeBookingApi = FakeBookingApi().apply {
            response = BookingListResponse(
                content = listOf(
                    buildBookingResponse(
                        id = 1L,
                        assetName = "Meeting Room 12",
                        categoryName = "Meeting room",
                        bookingPeriod = "HOUR",
                        bookingEnd = Instant.now().plus(2, ChronoUnit.DAYS).toString()
                    ),
                    buildBookingResponse(
                        id = 2L,
                        assetName = "Parking Spot 10",
                        categoryName = "",
                        bookingPeriod = "DAY",
                        bookingEnd = Instant.now().minus(2, ChronoUnit.DAYS).toString()
                    )
                )
            )
        }

        val viewModel = BookingsViewModel(
            bookingRepository = BookingRepository(fakeBookingApi),
            authSession = authSession
        )

        viewModel.refreshBookingsData()
        advanceUntilIdle()

        assertFalse(viewModel.uiState.value.isLoading)
        assertEquals(1, viewModel.uiState.value.myBookings.size)
        assertEquals(1, viewModel.uiState.value.historyBookings.size)
        assertEquals("Meeting Room 12", viewModel.uiState.value.myBookings.first().assetName)
        assertTrue(viewModel.uiState.value.myBookings.first().isHourlyBooking)
        assertEquals("-", viewModel.uiState.value.historyBookings.first().categoryName)
        assertEquals(null, viewModel.uiState.value.errorMessageResId)
    }

    @Test
    fun testShowsErrorWhenUserMissing() = runTest {
        val authSession = mock(AuthSession::class.java)
        `when`(authSession.getCurrentUserId()).thenReturn(null)

        val viewModel = BookingsViewModel(
            bookingRepository = BookingRepository(FakeBookingApi()),
            authSession = authSession
        )

        viewModel.refreshBookingsData()
        advanceUntilIdle()

        assertFalse(viewModel.uiState.value.isLoading)
        assertEquals(R.string.common_error_missing_logged_in_user, viewModel.uiState.value.errorMessageResId)
        assertTrue(viewModel.uiState.value.myBookings.isEmpty())
        assertTrue(viewModel.uiState.value.historyBookings.isEmpty())
    }

    @Test
    fun testShowsErrorOnInitialLoadFailure() = runTest {
        val authSession = mock(AuthSession::class.java)
        `when`(authSession.getCurrentUserId()).thenReturn(2L)

        val fakeBookingApi = FakeBookingApi().apply {
            getBookingsException = RuntimeException("Booking request failed")
        }

        val viewModel = BookingsViewModel(
            bookingRepository = BookingRepository(fakeBookingApi),
            authSession = authSession
        )

        viewModel.refreshBookingsData()
        advanceUntilIdle()

        assertFalse(viewModel.uiState.value.isLoading)
        assertEquals(R.string.bookings_error_load_message, viewModel.uiState.value.errorMessageResId)
    }

    @Test
    fun testKeepsExistingDataWhenRefreshFails() = runTest {
        val authSession = mock(AuthSession::class.java)
        `when`(authSession.getCurrentUserId()).thenReturn(2L)

        val futureBooking = buildBookingResponse(
            id = 1L,
            bookingEnd = Instant.now().plus(1, ChronoUnit.DAYS).toString()
        )
        val fakeBookingApi = FakeBookingApi().apply {
            response = BookingListResponse(content = listOf(futureBooking))
        }

        val viewModel = BookingsViewModel(
            bookingRepository = BookingRepository(fakeBookingApi),
            authSession = authSession
        )

        viewModel.refreshBookingsData()
        advanceUntilIdle()

        fakeBookingApi.getBookingsException = RuntimeException("Refresh failed")

        viewModel.refreshBookingsData()
        advanceUntilIdle()

        assertFalse(viewModel.uiState.value.isLoading)
        assertEquals(1, viewModel.uiState.value.myBookings.size)
        assertEquals(null, viewModel.uiState.value.errorMessageResId)
    }

    @Test
    fun testSearchFiltersActiveAndHistoryBookings() = runTest {
        val authSession = mock(AuthSession::class.java)
        `when`(authSession.getCurrentUserId()).thenReturn(2L)

        val fakeBookingApi = FakeBookingApi().apply {
            response = BookingListResponse(
                content = listOf(
                    buildBookingResponse(
                        id = 1L,
                        assetName = "Meeting Room 12",
                        categoryName = "Meeting room",
                        status = "APPROVED",
                        bookingEnd = Instant.now().plus(2, ChronoUnit.DAYS).toString()
                    ),
                    buildBookingResponse(
                        id = 2L,
                        assetName = "Parking Spot 10",
                        categoryName = "Parking",
                        status = "CANCELLED",
                        bookingEnd = Instant.now().minus(2, ChronoUnit.DAYS).toString()
                    )
                )
            )
        }

        val viewModel = BookingsViewModel(
            bookingRepository = BookingRepository(fakeBookingApi),
            authSession = authSession
        )

        viewModel.refreshBookingsData()
        advanceUntilIdle()
        viewModel.onSearchTextChange("parking")

        assertTrue(viewModel.uiState.value.filteredMyBookings.isEmpty())
        assertEquals(1, viewModel.uiState.value.filteredHistoryBookings.size)
        assertEquals("Parking Spot 10", viewModel.uiState.value.filteredHistoryBookings.first().assetName)
    }

    @Test
    fun testSearchFiltersHistoryBookingsByCategoryName() = runTest {
        val authSession = mock(AuthSession::class.java)
        `when`(authSession.getCurrentUserId()).thenReturn(2L)

        val fakeBookingApi = FakeBookingApi().apply {
            response = BookingListResponse(
                content = listOf(
                    buildBookingResponse(
                        id = 1L,
                        assetName = "Meeting Room 12",
                        categoryName = "Meeting room",
                        status = "APPROVED",
                        bookingEnd = Instant.now().minus(2, ChronoUnit.DAYS).toString()
                    ),
                    buildBookingResponse(
                        id = 2L,
                        assetName = "Parking Spot 10",
                        categoryName = "Parking",
                        status = "CANCELLED",
                        bookingEnd = Instant.now().minus(3, ChronoUnit.DAYS).toString()
                    )
                )
            )
        }

        val viewModel = BookingsViewModel(
            bookingRepository = BookingRepository(fakeBookingApi),
            authSession = authSession
        )

        viewModel.refreshBookingsData()
        advanceUntilIdle()
        viewModel.onSearchTextChange("meeting room")

        assertTrue(viewModel.uiState.value.filteredMyBookings.isEmpty())
        assertEquals(1, viewModel.uiState.value.filteredHistoryBookings.size)
        assertEquals("Meeting room", viewModel.uiState.value.filteredHistoryBookings.first().categoryName)
    }

    private fun buildBookingResponse(
        id: Long,
        assetName: String = "Parking Spot 10",
        categoryName: String = "Parking",
        bookingPeriod: String = "DAY",
        status: String = "PENDING",
        bookingEnd: String
    ) = BookingResponse(
        id = id,
        user = UserSummary(
            id = 2L,
            name = "Ivan",
            surname = "Horvat",
            email = "ivan@example.com",
            role = "USER",
            managerEmail = "manager@example.com"
        ),
        asset = AssetSummary(
            id = 1L,
            name = assetName,
            category = CategorySummary(
                id = 3L,
                name = categoryName,
                bookingPeriod = bookingPeriod,
                approval = true
            ),
            status = "ACTIVE",
            description = "Outdoor parking",
            location = "Level -2"
        ),
        status = status,
        bookingStart = Instant.now().minus(1, ChronoUnit.DAYS).toString(),
        bookingEnd = bookingEnd,
        notes = null
    )

    private class FakeBookingApi : BookingApi {
        var response: BookingListResponse = BookingListResponse(content = emptyList())
        var getBookingsException: Exception? = null

        override suspend fun getBookings(
            userId: Long?,
            assetId: Long?,
            status: String?,
            page: Int,
            size: Int
        ): BookingListResponse {
            getBookingsException?.let { throw it }

            val filteredContent = response.content.filter { booking ->
                val matchesUser = userId == null || booking.user.id == userId
                val matchesAsset = assetId == null || booking.asset.id == assetId
                val matchesStatus = status == null || booking.status.equals(status, ignoreCase = true)
                matchesUser && matchesAsset && matchesStatus
            }

            return response.copy(content = filteredContent.take(size))
        }

        override suspend fun createBooking(request: BookingCreateRequest): BookingResponse {
            error("createBooking is not used in BookingsViewModel tests.")
        }

        override suspend fun createRecurringBooking(request: RecurringBookingCreateRequest): List<BookingResponse> {
            error("createRecurringBooking is not used in BookingsViewModel tests.")
        }

        override suspend fun approveBooking(bookingId: Long): BookingResponse {
            error("approveBooking is not used in BookingsViewModel tests.")
        }

        override suspend fun rejectBooking(bookingId: Long): BookingResponse {
            error("rejectBooking is not used in BookingsViewModel tests.")
        }

        override suspend fun updateBooking(
            bookingId: Long,
            request: BookingStatusUpdateRequest
        ): BookingResponse {
            error("updateBooking is not used in BookingsViewModel tests.")
        }
    }
}
