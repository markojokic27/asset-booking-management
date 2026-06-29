package com.example.assetbookingmanagement.features.booking.ui

import androidx.compose.ui.res.stringResource
import com.example.assetbookingmanagement.R
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.assetbookingmanagement.core.ui.components.AppCard
import com.example.assetbookingmanagement.core.ui.components.AppEmptyState
import com.example.assetbookingmanagement.core.ui.components.AppLoadingState
import com.example.assetbookingmanagement.core.ui.components.AppMessageState
import com.example.assetbookingmanagement.core.ui.components.SearchBar
import com.example.assetbookingmanagement.core.ui.components.StatusBadge
import com.example.assetbookingmanagement.core.ui.format.formatLocalizedBookingPeriod

@Composable
fun ApprovalRequestsScreen(
    onApprovalRequestClick: (ApprovalRequestUiModel) -> Unit = {},
    viewModel: ApprovalRequestsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadApprovalRequests()
    }

    Column(
        modifier = Modifier
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (uiState.requests.isNotEmpty()) {
            SearchBar(
                value = uiState.searchText,
                onValueChange = viewModel::onSearchTextChange,
                placeholder = stringResource(R.string.common_search_bookings_placeholder)
            )
        }

        when {
            uiState.isLoading -> {
                AppLoadingState()
            }

            uiState.errorMessageResId != null -> {
                val errorMessageResId = uiState.errorMessageResId ?: return@Column
                AppMessageState(
                    title = stringResource(R.string.approvals_error_load_title),
                    message = stringResource(errorMessageResId)
                )
            }

            uiState.filteredRequests.isEmpty() -> {
                AppEmptyState(
                    text = if (uiState.requests.isEmpty()) {
                        stringResource(R.string.approvals_empty_no_requests)
                    } else {
                        stringResource(R.string.approvals_empty_no_matching)
                    }
                )
            }

            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        items = uiState.filteredRequests,
                        key = { request -> request.id }
                    ) { request ->
                        ApprovalRequestCard(
                            request = request,
                            onClick = { onApprovalRequestClick(request) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ApprovalRequestCard(
    request: ApprovalRequestUiModel,
    onClick: () -> Unit
) {
    val context = LocalContext.current

    AppCard(onClick = onClick) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = request.assetName,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = request.requesterName,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = formatLocalizedBookingPeriod(
                    context = context,
                    bookingStart = request.bookingStart,
                    bookingEnd = request.bookingEnd,
                    isHourlyBooking = request.isHourlyBooking
                ),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.55f)
            )
        }

        StatusBadge(status = request.status)
    }
}
