package com.example.assetbookingmanagement.features.booking.ui

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

/**
 * Formats a booking date/time for display in the UI.
 * Shows date and time for hourly bookings and only the date for daily bookings.
 */
fun Instant.toBookingDisplayText(isHourlyBooking: Boolean): String {
    val zonedDateTime = atZone(ZoneId.systemDefault())
    return if (isHourlyBooking) {
        zonedDateTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))
    } else {
        zonedDateTime.toLocalDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
    }
}
