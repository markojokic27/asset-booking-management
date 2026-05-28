package com.example.assetbookingmanagement.features.booking.data

import kotlinx.serialization.Serializable

@Serializable
data class BookingCreateRequest(
    val userId: Long,
    val assetId: Long,
    val bookingStart: String,
    val bookingEnd: String,
    val notes: String? = null
)
