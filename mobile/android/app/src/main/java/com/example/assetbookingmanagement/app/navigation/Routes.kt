package com.example.assetbookingmanagement.app.navigation

import android.net.Uri

object Routes {
    // Route names
    const val LOGIN = "login"
    const val HOME = "home"
    const val ASSETS = "assets"
    const val ASSET_DETAILS = "asset_details/{assetId}"
    const val BOOKINGS = "bookings"
    const val PROFILE = "profile"
    const val CREATE_BOOKING = "create_booking/{assetId}"
    const val BOOKING_SUCCESS =
        "booking_success?assetName={assetName}&fromDate={fromDate}&toDate={toDate}"

    fun assetDetails(assetId: Long) = "asset_details/$assetId"

    fun createBooking(assetId: Long) = "create_booking/$assetId"

    fun bookingSuccess(
        assetName: String,
        fromDate: String,
        toDate: String
    ) = "booking_success?assetName=${Uri.encode(assetName)}&fromDate=${Uri.encode(fromDate)}&toDate=${Uri.encode(toDate)}"
}
