package com.example.assetbookingmanagement.app.navigation

import android.net.Uri

object Routes {
    // Route names
    const val LOGIN = "login"
    const val HOME = "home"
    const val ASSETS = "assets"
    const val ASSET_DETAILS = "asset_details/{assetId}"
    const val BOOKINGS = "bookings"
    const val APPROVAL_REQUESTS = "approval_requests"
    const val APPROVAL_REQUEST_DETAILS =
        "approval_request_details/{bookingId}?assetName={assetName}&requesterName={requesterName}&fromDate={fromDate}&toDate={toDate}&status={status}&isHourlyBooking={isHourlyBooking}"
    const val BOOKING_DETAILS =
        "booking_details/{bookingId}?assetName={assetName}&fromDate={fromDate}&toDate={toDate}&status={status}&categoryName={categoryName}&isHourlyBooking={isHourlyBooking}"
    const val PROFILE = "profile"
    const val CHANGE_PASSWORD = "change_password"
    const val CREATE_BOOKING = "create_booking/{assetId}"
    const val BOOKING_SUCCESS =
        "booking_success?assetName={assetName}&fromDate={fromDate}&toDate={toDate}&approvalRequired={approvalRequired}"

    fun assetDetails(assetId: Long) = "asset_details/$assetId"

    fun approvalRequestDetails(
        bookingId: Long,
        assetName: String,
        requesterName: String,
        fromDate: String,
        toDate: String,
        status: String,
        isHourlyBooking: Boolean
    ) = "approval_request_details/$bookingId?assetName=${Uri.encode(assetName)}&requesterName=${Uri.encode(requesterName)}&fromDate=${Uri.encode(fromDate)}&toDate=${Uri.encode(toDate)}&status=${Uri.encode(status)}&isHourlyBooking=$isHourlyBooking"

    fun bookingDetails(
        bookingId: Long,
        assetName: String,
        fromDate: String,
        toDate: String,
        status: String,
        categoryName: String,
        isHourlyBooking: Boolean
    ) = "booking_details/$bookingId?assetName=${Uri.encode(assetName)}&fromDate=${Uri.encode(fromDate)}&toDate=${Uri.encode(toDate)}&status=${Uri.encode(status)}&categoryName=${Uri.encode(categoryName)}&isHourlyBooking=$isHourlyBooking"

    fun createBooking(assetId: Long) = "create_booking/$assetId"

    fun bookingSuccess(
        assetName: String,
        fromDate: String,
        toDate: String,
        approvalRequired: Boolean
    ) = "booking_success?assetName=${Uri.encode(assetName)}&fromDate=${Uri.encode(fromDate)}&toDate=${Uri.encode(toDate)}&approvalRequired=$approvalRequired"
}
