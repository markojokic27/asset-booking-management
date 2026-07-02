package com.example.assetbookingmanagement.features.booking.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.assetbookingmanagement.R
import com.example.assetbookingmanagement.core.ui.components.AppButton
import com.example.assetbookingmanagement.core.ui.format.formatLocalizedBookingDisplayText
import com.example.assetbookingmanagement.core.ui.components.DetailsRow
import com.example.assetbookingmanagement.core.ui.components.DetailsSectionCard
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
    isHourlyBooking: Boolean,
    onCancelled: () -> Unit,
    viewModel: BookingDetailsViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val unavailableText = stringResource(R.string.common_value_unavailable)
    val cancelError = uiState.errorMessageResId?.let { stringResource(it) }
    var showCancelDialog by remember { mutableStateOf(false) }
    val canCancelBooking = canCancelBooking(status = status, bookingEnd = bookingEnd)

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
                categoryName = categoryName.ifBlank { unavailableText },
                canCancelBooking = canCancelBooking,
                isCancelling = uiState.isCancelling,
                cancelError = cancelError,
                onCancelClick = { showCancelDialog = true }
            )
        }
    }

    if (showCancelDialog) {
        AlertDialog(
            onDismissRequest = {
                if (!uiState.isCancelling) {
                    showCancelDialog = false
                }
            },
            shape = RoundedCornerShape(14.dp),
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
            textContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            title = {
                Text(
                    text = stringResource(R.string.bookings_cancel_dialog_title),
                    style = MaterialTheme.typography.titleLarge
                )
            },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = stringResource(R.string.bookings_cancel_dialog_message),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    if (cancelError != null) {
                        Text(
                            text = cancelError,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.cancelBooking(bookingId = bookingId) {
                            showCancelDialog = false
                            onCancelled()
                        }
                    },
                    enabled = !uiState.isCancelling,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError
                    )
                ) {
                    Text(
                        text = if (uiState.isCancelling) {
                            stringResource(R.string.bookings_cancel_action_loading)
                        } else {
                            stringResource(R.string.bookings_cancel_action)
                        }
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showCancelDialog = false },
                    enabled = !uiState.isCancelling
                ) {
                    Text(text = stringResource(R.string.common_cancel))
                }
            }
        )
    }
}

@Composable
private fun BookingSummarySection(
    bookingId: Long,
    assetName: String,
    bookingStart: String,
    bookingEnd: String,
    status: String,
    categoryName: String,
    canCancelBooking: Boolean,
    isCancelling: Boolean,
    cancelError: String?,
    onCancelClick: () -> Unit
) {
    DetailsSectionCard(
        title = stringResource(R.string.nav_approval_request_details_title, bookingId),
        heading = assetName
    ) {
        BookingInfoRow(label = stringResource(R.string.common_from), value = bookingStart, showDivider = true)
        BookingInfoRow(label = stringResource(R.string.common_to), value = bookingEnd, showDivider = true)
        BookingStatusRow(status = status, showDivider = true)
        BookingInfoRow(label = stringResource(R.string.common_category), value = categoryName, showDivider = false)

        if (canCancelBooking) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AppButton(
                    text = if (isCancelling) {
                        stringResource(R.string.bookings_cancel_action_loading)
                    } else {
                        stringResource(R.string.bookings_cancel_action)
                    },
                    onClick = onCancelClick,
                    enabled = !isCancelling
                )

                cancelError?.let { error ->
                    Text(
                        text = error,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
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

private fun canCancelBooking(
    status: String,
    bookingEnd: String
): Boolean {
    if (status in setOf("CANCELLED", "REJECTED", "COMPLETED")) {
        return false
    }

    val endInstant = runCatching { Instant.parse(bookingEnd) }.getOrNull() ?: return false
    return endInstant.isAfter(Instant.now())
}
