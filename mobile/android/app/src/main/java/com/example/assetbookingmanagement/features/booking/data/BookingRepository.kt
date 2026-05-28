package com.example.assetbookingmanagement.features.booking.data

import javax.inject.Inject

class BookingRepository @Inject constructor(
    private val bookingApi: BookingApi
) {
    suspend fun getUserBookings(userId: Long): List<BookingResponse> {
        return bookingApi.getBookings(userId = userId).content
    }

    suspend fun createBooking(request: BookingCreateRequest): BookingResponse {
        return bookingApi.createBooking(request)
    }
}
