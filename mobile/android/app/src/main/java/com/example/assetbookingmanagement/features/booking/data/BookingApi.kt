package com.example.assetbookingmanagement.features.booking.data

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface BookingApi {

    @GET("bookings")
    suspend fun getBookings(
        @Query("userId") userId: Long,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 50
    ): BookingListResponse

    @POST("bookings")
    suspend fun createBooking(
        @Body request: BookingCreateRequest
    ): BookingResponse
}
