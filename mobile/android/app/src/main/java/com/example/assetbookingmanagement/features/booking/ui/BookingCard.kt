package com.example.assetbookingmanagement.features.booking.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.example.assetbookingmanagement.core.ui.components.AppCard
import com.example.assetbookingmanagement.core.ui.components.StatusBadge

@Composable
fun BookingCard(
    booking: MyBookingUiModel
) {
    AppCard {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = booking.assetName,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = booking.bookingPeriod,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.55f)
            )
        }

        StatusBadge(status = booking.status)
    }
}
