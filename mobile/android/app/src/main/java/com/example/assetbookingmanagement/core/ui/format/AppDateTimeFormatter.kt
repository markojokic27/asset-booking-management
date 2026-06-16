package com.example.assetbookingmanagement.core.ui.format

import android.content.Context
import android.text.format.DateFormat
import java.time.Instant
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

private const val DATE_SKELETON = "ddMMyyyy"
private const val DATE_TIME_SKELETON_24_HOUR = "ddMMyyyyHm"
private const val DATE_TIME_SKELETON_12_HOUR = "ddMMyyyyhm"
private const val TIME_SKELETON_24_HOUR = "Hm"
private const val TIME_SKELETON_12_HOUR = "hm"

/** Formats a booking instant for display using the device date and time settings. */
fun formatLocalizedBookingDisplayText(
    instant: Instant,
    context: Context,
    isHourlyBooking: Boolean
): String =
    instant.atZone(ZoneId.systemDefault()).format(
        bookingFormatter(context, isHourlyBooking)
    )

/** Parses and formatsa booking date-time string and returns a fallback value if parsing fails. */
fun formatLocalizedBookingDisplayText(
    dateTimeText: String,
    context: Context,
    isHourlyBooking: Boolean,
    fallback: String = dateTimeText.ifBlank { "-" }
): String =
    runCatching {
        formatLocalizedBookingDisplayText(
            instant = Instant.parse(dateTimeText),
            context = context,
            isHourlyBooking = isHourlyBooking
        )
    }.getOrDefault(fallback)

fun formatLocalizedBookingPeriod(
    context: Context,
    bookingStart: String,
    bookingEnd: String,
    isHourlyBooking: Boolean,
    separator: String = " - "
): String {
    val formattedStart = formatLocalizedBookingDisplayText(
        dateTimeText = bookingStart,
        context = context,
        isHourlyBooking = isHourlyBooking
    )
    val formattedEnd = formatLocalizedBookingDisplayText(
        dateTimeText = bookingEnd,
        context = context,
        isHourlyBooking = isHourlyBooking
    )

    return "$formattedStart$separator$formattedEnd"
}

fun formatLocalizedDate(
    millis: Long
): String =
    Instant.ofEpochMilli(millis)
        .atZone(ZoneId.of("UTC"))
        .toLocalDate()
        .format(dateFormatter())

fun formatLocalizedTime(
    context: Context,
    hour: Int,
    minute: Int
): String =
    LocalTime.of(hour, minute).format(timeFormatter(context))

private fun bookingFormatter(
    context: Context,
    isHourlyBooking: Boolean
): DateTimeFormatter =
    localizedFormatter(
        skeleton = if (isHourlyBooking) {
            if (DateFormat.is24HourFormat(context)) {
                DATE_TIME_SKELETON_24_HOUR
            } else {
                DATE_TIME_SKELETON_12_HOUR
            }
        } else {
            DATE_SKELETON
        }
    )

private fun dateFormatter(): DateTimeFormatter =
    localizedFormatter(DATE_SKELETON)

private fun timeFormatter(context: Context): DateTimeFormatter =
    localizedFormatter(
        if (DateFormat.is24HourFormat(context)) {
            TIME_SKELETON_24_HOUR
        } else {
            TIME_SKELETON_12_HOUR
        }
    )

private fun localizedFormatter(skeleton: String): DateTimeFormatter {
    val locale = Locale.getDefault()
    return DateTimeFormatter.ofPattern(
        DateFormat.getBestDateTimePattern(locale, skeleton),
        locale
    )
}
