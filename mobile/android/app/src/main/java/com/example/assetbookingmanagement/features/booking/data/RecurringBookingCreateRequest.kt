package com.example.assetbookingmanagement.features.booking.data

import kotlinx.serialization.Serializable

@Serializable
data class TimeSlotRequest(
    val bookingStart: String,
    val bookingEnd: String
)

@Serializable
data class RecurringBookingCreateRequest(
    val userId: Long,
    val assetId: Long,
    val timeSlots: List<TimeSlotRequest>,
    val notes: String? = null
)
