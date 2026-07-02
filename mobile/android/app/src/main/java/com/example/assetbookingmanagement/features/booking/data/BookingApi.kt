package com.example.assetbookingmanagement.features.booking.data

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.Path

interface BookingApi {

    @GET("bookings")
    suspend fun getBookings(
        @Query("userId") userId: Long? = null,
        @Query("assetId") assetId: Long? = null,
        @Query("status") status: String? = null,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 50
    ): BookingListResponse

    @POST("bookings")
    suspend fun createBooking(
        @Body request: BookingCreateRequest
    ): BookingResponse

    @POST("bookings/recurring")
    suspend fun createRecurringBooking(
        @Body request: RecurringBookingCreateRequest
    ): List<BookingResponse>

    @POST("bookings/{bookingId}/approve")
    suspend fun approveBooking(
        @Path("bookingId") bookingId: Long
    ): BookingResponse

    @POST("bookings/{bookingId}/reject")
    suspend fun rejectBooking(
        @Path("bookingId") bookingId: Long
    ): BookingResponse

    @PATCH("bookings/{bookingId}")
    suspend fun updateBooking(
        @Path("bookingId") bookingId: Long,
        @Body request: BookingStatusUpdateRequest
    ): BookingResponse
}
