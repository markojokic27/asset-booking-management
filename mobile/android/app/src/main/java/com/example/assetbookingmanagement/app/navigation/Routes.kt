package com.example.assetbookingmanagement.app.navigation

object Routes {
    // Route names
    const val LOGIN = "login"
    const val HOME = "home"
    const val ASSETS = "assets"
    const val ASSET_DETAILS = "asset_details/{assetId}"
    const val BOOKINGS = "bookings"
    const val PROFILE = "profile"
    const val CREATE_BOOKING = "create_booking/{assetId}"

    fun assetDetails(assetId: Long) = "asset_details/$assetId"

    fun createBooking(assetId: Long) = "create_booking/$assetId"
}
