package com.example.assetbookingmanagement.features.booking.ui

import androidx.compose.ui.res.stringResource
import com.example.assetbookingmanagement.R
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

data class ApprovalRequestDetailsUiModel(
    val bookingId: Long,
    val assetName: String,
    val requesterName: String,
    val bookingStart: String,
    val bookingEnd: String,
    val status: String,
    val isHourlyBooking: Boolean
)

@Composable
fun ApprovalRequestDetailsScreen(
    details: ApprovalRequestDetailsUiModel,
    onApproved: () -> Unit,
    onRejected: () -> Unit,
    viewModel: ApprovalRequestDetailsViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val unavailableText = stringResource(R.string.common_value_unavailable)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 20.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        DetailsSectionCard(
            title = stringResource(R.string.approvals_details_section_title),
            heading = details.assetName.ifBlank {
                stringResource(R.string.nav_approval_request_details_title, details.bookingId)
            }
        ) {
            RequestInfoRow(
                label = stringResource(R.string.approvals_details_booked_by_label),
                value = details.requesterName.ifBlank { unavailableText },
                showDivider = true
            )
            RequestInfoRow(
                label = stringResource(R.string.common_from),
                value = formatLocalizedBookingDisplayText(
                    details.bookingStart,
                    context,
                    details.isHourlyBooking
                ),
                showDivider = true
            )
            RequestInfoRow(
                label = stringResource(R.string.common_to),
                value = formatLocalizedBookingDisplayText(
                    details.bookingEnd,
                    context,
                    details.isHourlyBooking
                ),
                showDivider = true
            )
            RequestStatusRow(
                label = stringResource(R.string.common_status),
                status = details.status.ifBlank { unavailableText },
                showDivider = uiState.errorMessageResId == null
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
                            bookingId = details.bookingId,
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
                        text = stringResource(R.string.approvals_action_reject),
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Button(
                    onClick = {
                        viewModel.approveBooking(
                            bookingId = details.bookingId,
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
                        text = stringResource(R.string.approvals_action_approve),
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
            uiState.errorMessageResId?.let { errorMessageResId ->
                Text(
                    text = stringResource(errorMessageResId),
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
    label: String,
    status: String,
    showDivider: Boolean
) {
    DetailsRow(showDivider = showDivider) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        StatusBadge(status = status)
    }
}
