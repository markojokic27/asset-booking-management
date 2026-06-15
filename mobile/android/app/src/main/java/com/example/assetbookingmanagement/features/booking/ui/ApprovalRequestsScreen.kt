package com.example.assetbookingmanagement.features.booking.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.assetbookingmanagement.core.ui.components.AppCard
import com.example.assetbookingmanagement.core.ui.components.StatusBadge

@Composable
fun ApprovalRequestsScreen(
    onApprovalRequestClick: (ApprovalRequestUiModel) -> Unit = {},
    viewModel: ApprovalRequestsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        when {
            uiState.isLoading -> {
                Text(text = "Loading approval requests...")
            }

            uiState.errorMessage != null -> {
                Text(text = uiState.errorMessage ?: "")
            }

            uiState.requests.isEmpty() -> {
                Text(text = "No pending requests.")
            }

            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        items = uiState.requests,
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
                text = request.bookingPeriod,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.55f)
            )
        }

        StatusBadge(status = request.status)
    }
}
