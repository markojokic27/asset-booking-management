package com.example.assetbookingmanagement.features.booking.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.assetbookingmanagement.core.ui.format.formatLocalizedBookingDisplayText
import com.example.assetbookingmanagement.core.ui.components.DetailsRow
import com.example.assetbookingmanagement.core.ui.components.DetailsSectionCard
import com.example.assetbookingmanagement.core.ui.components.StatusBadge

@Composable
fun BookingDetailsScreen(
    bookingId: Long,
    assetName: String,
    bookingStart: String,
    bookingEnd: String,
    status: String,
    categoryName: String,
    isHourlyBooking: Boolean
) {
    val context = LocalContext.current

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item(key = bookingId) {
            BookingSummarySection(
                assetName = assetName.ifBlank { "Booking $bookingId" },
                bookingStart = formatLocalizedBookingDisplayText(bookingStart, context, isHourlyBooking),
                bookingEnd = formatLocalizedBookingDisplayText(bookingEnd, context, isHourlyBooking),
                status = status.ifBlank { "-" },
                categoryName = categoryName.ifBlank { "-" }
            )
        }
    }
}

@Composable
private fun BookingSummarySection(
    assetName: String,
    bookingStart: String,
    bookingEnd: String,
    status: String,
    categoryName: String
) {
    DetailsSectionCard(
        title = "BOOKING SUMMARY",
        heading = assetName
    ) {
        BookingInfoRow(label = "From", value = bookingStart, showDivider = true)
        BookingInfoRow(label = "To", value = bookingEnd, showDivider = true)
        BookingStatusRow(status = status, showDivider = true)
        BookingInfoRow(label = "Category", value = categoryName, showDivider = false)
    }
}

@Composable
private fun BookingInfoRow(
    label: String,
    value: String,
    showDivider: Boolean
) {
    DetailsRow(showDivider = showDivider) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun BookingStatusRow(
    status: String,
    showDivider: Boolean
) {
    DetailsRow(showDivider = showDivider) {
        Text(
            text = "Status",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        StatusBadge(status = status)
    }
}
