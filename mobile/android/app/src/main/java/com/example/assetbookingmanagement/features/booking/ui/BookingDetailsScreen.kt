package com.example.assetbookingmanagement.features.booking.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.assetbookingmanagement.core.ui.components.StatusBadge
import java.time.Instant

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
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item(key = bookingId) {
            BookingSummarySection(
                assetName = assetName.ifBlank { "Booking $bookingId" },
                bookingStart = bookingStart.toDisplayDateTime(isHourlyBooking),
                bookingEnd = bookingEnd.toDisplayDateTime(isHourlyBooking),
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
    BookingSectionCard(
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
private fun BookingSectionCard(
    title: String,
    heading: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 18.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = heading,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Column(content = content)
        }
    }
}

@Composable
private fun BookingInfoRow(
    label: String,
    value: String,
    showDivider: Boolean
) {
    BookingDetailRow(showDivider = showDivider) {
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
    BookingDetailRow(showDivider = showDivider) {
        Text(
            text = "Status",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        StatusBadge(status = status)
    }
}

@Composable
private fun BookingDetailRow(
    showDivider: Boolean,
    content: @Composable RowScope.() -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            content = content
        )

        if (showDivider) {
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
        }
    }
}

private fun String.toDisplayDateTime(isHourlyBooking: Boolean): String =
    runCatching { Instant.parse(this).toBookingDisplayText(isHourlyBooking = isHourlyBooking) }
        .getOrDefault(ifBlank { "-" })
