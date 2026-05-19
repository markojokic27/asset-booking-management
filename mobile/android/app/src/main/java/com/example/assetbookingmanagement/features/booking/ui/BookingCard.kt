package com.example.assetbookingmanagement.features.booking.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.assetbookingmanagement.core.ui.theme.BookingStatusApprovedBg
import com.example.assetbookingmanagement.core.ui.theme.BookingStatusCancelledBg
import com.example.assetbookingmanagement.core.ui.theme.BookingStatusCompletedBg
import com.example.assetbookingmanagement.core.ui.theme.BookingStatusPendingBg
import com.example.assetbookingmanagement.core.ui.theme.BookingStatusRejectedBg
import com.example.assetbookingmanagement.core.ui.theme.Gray100
import com.example.assetbookingmanagement.core.ui.theme.TextLight

@Composable
fun BookingCard(
    booking: MyBookingUiModel
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f),
                shape = RoundedCornerShape(10.dp)
            ),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
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

            BookingStatusBadge(status = booking.status)
        }
    }
}

private fun bookingStatusColor(status: String) = when (status.uppercase()) {
    "APPROVED" -> BookingStatusApprovedBg
    "PENDING" -> BookingStatusPendingBg
    "CANCELLED" -> BookingStatusCancelledBg
    "REJECTED" -> BookingStatusRejectedBg
    "COMPLETED" -> BookingStatusCompletedBg
    else -> Gray100
}

@Composable
private fun BookingStatusBadge(status: String) {
    Surface(
        color = bookingStatusColor(status),
        shape = CircleShape
    ) {
        Text(
            text = status,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            color = TextLight
        )
    }
}
