package com.example.assetbookingmanagement.features.booking.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.assetbookingmanagement.core.ui.components.DetailsRow
import com.example.assetbookingmanagement.core.ui.components.DetailsSectionCard
import com.example.assetbookingmanagement.core.ui.components.StatusBadge
import com.example.assetbookingmanagement.core.ui.format.formatLocalizedBookingDisplayText

@Composable
fun ApprovalRequestDetailsScreen(
    bookingId: Long,
    assetName: String,
    requesterName: String,
    bookingStart: String,
    bookingEnd: String,
    status: String,
    isHourlyBooking: Boolean,
    onApproved: () -> Unit,
    onRejected: () -> Unit,
    viewModel: ApprovalRequestDetailsViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 20.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        DetailsSectionCard(
            title = "REQUEST DETAILS",
            heading = assetName.ifBlank { "Request #$bookingId" }
        ) {
            RequestInfoRow(
                label = "Booked by",
                value = requesterName.ifBlank { "-" },
                showDivider = true
            )
            RequestInfoRow(
                label = "From",
                value = formatLocalizedBookingDisplayText(bookingStart, context, isHourlyBooking),
                showDivider = true
            )
            RequestInfoRow(
                label = "To",
                value = formatLocalizedBookingDisplayText(bookingEnd, context, isHourlyBooking),
                showDivider = true
            )
            RequestStatusRow(
                status = status.ifBlank { "-" },
                showDivider = false
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = {
                        viewModel.rejectBooking(
                            bookingId = bookingId,
                            onSuccess = onRejected
                        )
                    },
                    enabled = !uiState.isSubmitting,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.28f),
                        contentColor = MaterialTheme.colorScheme.onSurface
                    ),
                    border = BorderStroke(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f)
                    )
                ) {
                    Text(
                        text = "Reject",
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Button(
                    onClick = {
                        viewModel.approveBooking(
                            bookingId = bookingId,
                            onSuccess = onApproved
                        )
                    },
                    enabled = !uiState.isSubmitting,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(
                        text = "Approve",
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
            uiState.errorMessage?.let { errorMessage ->
                Text(
                    text = errorMessage,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}

@Composable
private fun RequestInfoRow(
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
private fun RequestStatusRow(
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
