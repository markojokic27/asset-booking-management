package com.example.assetbookingmanagement.features.booking.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import com.example.assetbookingmanagement.core.ui.components.AppCard
import com.example.assetbookingmanagement.core.ui.components.StatusBadge
import com.example.assetbookingmanagement.core.ui.format.formatLocalizedBookingPeriod

@Composable
fun BookingCard(
    booking: MyBookingUiModel,
    onClick: () -> Unit = {}
) {
    val context = LocalContext.current

    AppCard(onClick = onClick) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = booking.assetName,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = formatLocalizedBookingPeriod(
                    context = context,
                    bookingStart = booking.bookingStart,
                    bookingEnd = booking.bookingEnd,
                    isHourlyBooking = booking.isHourlyBooking
                ),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.55f)
            )
        }

        StatusBadge(status = booking.status)
    }
}
