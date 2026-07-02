package com.example.assetbookingmanagement.features.booking.data

import javax.inject.Inject

class BookingRepository @Inject constructor(
    private val bookingApi: BookingApi
) {
    suspend fun getUserBookings(userId: Long): List<BookingResponse> {
        return bookingApi.getBookings(userId = userId).content
    }

    suspend fun getAssetBookings(assetId: Long): List<BookingResponse> {
        return bookingApi.getBookings(assetId = assetId).content
    }

    suspend fun getPendingBookings(): List<BookingResponse> {
        return bookingApi.getBookings(status = "PENDING", size = 100).content
    }

    suspend fun createBooking(request: BookingCreateRequest): BookingResponse {
        return bookingApi.createBooking(request)
    }

    suspend fun createRecurringBooking(request: RecurringBookingCreateRequest): List<BookingResponse> {
        return bookingApi.createRecurringBooking(request)
    }

    suspend fun approveBooking(bookingId: Long): BookingResponse {
        return bookingApi.approveBooking(bookingId)
    }

    suspend fun rejectBooking(bookingId: Long): BookingResponse {
        return bookingApi.rejectBooking(bookingId)
    }

    suspend fun cancelBooking(bookingId: Long): BookingResponse {
        return bookingApi.updateBooking(
            bookingId = bookingId,
            request = BookingStatusUpdateRequest(status = "CANCELLED")
        )
    }
}
