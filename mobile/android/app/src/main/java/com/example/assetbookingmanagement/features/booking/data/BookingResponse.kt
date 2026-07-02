package com.example.assetbookingmanagement.features.booking.data

import kotlinx.serialization.Serializable

@Serializable
data class BookingResponse(
    val id: Long,
    val user: UserSummary,
    val asset: AssetSummary,
    val status: String,
    val bookingStart: String,
    val bookingEnd: String,
    val notes: String? = null
)

@Serializable
data class UserSummary(
    val id: Long,
    val name: String,
    val surname: String,
    val email: String,
    val role: String,
    val managerEmail: String? = null
)

@Serializable
data class AssetSummary(
    val id: Long,
    val name: String,
    val category: CategorySummary,
    val status: String,
    val description: String,
    val location: String
)

@Serializable
data class CategorySummary(
    val id: Long,
    val name: String,
    val bookingPeriod: String,
    val approval: Boolean
)

@Serializable
data class BookingListResponse(
    val content: List<BookingResponse>
)

@Serializable
data class BookingStatusUpdateRequest(
    val status: String
)
