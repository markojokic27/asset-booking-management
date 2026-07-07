package com.example.assetbookingmanagement.features.booking.ui

import com.example.assetbookingmanagement.R
import com.example.assetbookingmanagement.features.asset.ui.MainDispatcherRule
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
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BookingDetailsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun testCancelBookingUpdatesStateAndInvokesSuccess() = runTest {
        val fakeBookingApi = FakeBookingApi()
        val viewModel = BookingDetailsViewModel(
            bookingRepository = BookingRepository(fakeBookingApi)
        )
        var successCalled = false

        viewModel.cancelBooking(bookingId = 9L) {
            successCalled = true
        }
        advanceUntilIdle()

        assertEquals(listOf(9L), fakeBookingApi.updatedBookingIds)
        assertEquals(listOf(BookingStatusUpdateRequest(status = "CANCELLED")), fakeBookingApi.updateRequests)
        assertFalse(viewModel.uiState.value.isCancelling)
        assertNull(viewModel.uiState.value.errorMessageResId)
        assertTrue(successCalled)
    }

    @Test
    fun testShowsErrorWhenCancelBookingFails() = runTest {
        val fakeBookingApi = FakeBookingApi().apply {
            updateBookingException = RuntimeException("Cancel failed")
        }
        val viewModel = BookingDetailsViewModel(
            bookingRepository = BookingRepository(fakeBookingApi)
        )
        var successCalled = false

        viewModel.cancelBooking(bookingId = 9L) {
            successCalled = true
        }
        advanceUntilIdle()

        assertEquals(listOf(9L), fakeBookingApi.updatedBookingIds)
        assertEquals(listOf(BookingStatusUpdateRequest(status = "CANCELLED")), fakeBookingApi.updateRequests)
        assertFalse(viewModel.uiState.value.isCancelling)
        assertEquals(R.string.bookings_cancel_error, viewModel.uiState.value.errorMessageResId)
        assertFalse(successCalled)
    }

    private fun buildBookingResponse(id: Long) = BookingResponse(
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
            name = "Hp 15",
            category = CategorySummary(
                id = 1L,
                name = "Laptops",
                bookingPeriod = "DAY",
                approval = true
            ),
            status = "ACTIVE",
            description = "Laptop located in room 301",
            location = "Room 301"
        ),
        status = "PENDING",
        bookingStart = "2026-01-04T09:00:00Z",
        bookingEnd = "2026-01-14T09:00:00Z",
        notes = "Some optional notes"
    )

    private inner class FakeBookingApi : BookingApi {
        var updateBookingException: Exception? = null
        val updatedBookingIds = mutableListOf<Long>()
        val updateRequests = mutableListOf<BookingStatusUpdateRequest>()

        override suspend fun getBookings(
            userId: Long?,
            assetId: Long?,
            status: String?,
            page: Int,
            size: Int
        ): BookingListResponse {
            error("getBookings is not used in BookingDetailsViewModel tests.")
        }

        override suspend fun createBooking(request: BookingCreateRequest): BookingResponse {
            error("createBooking is not used in BookingDetailsViewModel tests.")
        }

        override suspend fun createRecurringBooking(request: RecurringBookingCreateRequest): List<BookingResponse> {
            error("createRecurringBooking is not used in BookingDetailsViewModel tests.")
        }

        override suspend fun approveBooking(bookingId: Long): BookingResponse {
            error("approveBooking is not used in BookingDetailsViewModel tests.")
        }

        override suspend fun rejectBooking(bookingId: Long): BookingResponse {
            error("rejectBooking is not used in BookingDetailsViewModel tests.")
        }

        override suspend fun updateBooking(
            bookingId: Long,
            request: BookingStatusUpdateRequest
        ): BookingResponse {
            updatedBookingIds += bookingId
            updateRequests += request
            updateBookingException?.let { throw it }
            return buildBookingResponse(bookingId)
        }
    }
}
