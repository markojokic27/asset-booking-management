package com.example.assetbookingmanagement.features.booking.data

import kotlinx.serialization.Serializable

@Serializable
data class BookingResponse(
    val id: Long,
    val userId: Long,
    val assetId: Long,
    val status: String,
    val bookingStart: String,
    val bookingEnd: String,
    val notes: String? = null
)

@Serializable
data class BookingListResponse(
    val content: List<BookingResponse>
)
