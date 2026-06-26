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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.assetbookingmanagement.R
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
    val unavailableText = stringResource(R.string.common_value_unavailable)

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item(key = bookingId) {
            BookingSummarySection(
                bookingId = bookingId,
                assetName = assetName.ifBlank { stringResource(R.string.nav_approval_request_details_title, bookingId) },
                bookingStart = formatLocalizedBookingDisplayText(bookingStart, context, isHourlyBooking),
                bookingEnd = formatLocalizedBookingDisplayText(bookingEnd, context, isHourlyBooking),
                status = status.ifBlank { unavailableText },
                categoryName = categoryName.ifBlank { unavailableText }
            )
        }
    }
}

@Composable
private fun BookingSummarySection(
    bookingId: Long,
    assetName: String,
    bookingStart: String,
    bookingEnd: String,
    status: String,
    categoryName: String
) {
    DetailsSectionCard(
        title = stringResource(R.string.nav_approval_request_details_title, bookingId),
        heading = assetName
    ) {
        BookingInfoRow(label = stringResource(R.string.common_from), value = bookingStart, showDivider = true)
        BookingInfoRow(label = stringResource(R.string.common_to), value = bookingEnd, showDivider = true)
        BookingStatusRow(status = status, showDivider = true)
        BookingInfoRow(label = stringResource(R.string.common_category), value = categoryName, showDivider = false)
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
            text = stringResource(R.string.common_status),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        StatusBadge(status = status)
    }
}
