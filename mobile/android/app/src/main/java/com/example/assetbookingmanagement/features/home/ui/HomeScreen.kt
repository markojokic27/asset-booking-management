package com.example.assetbookingmanagement.features.home.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.compose.LifecycleResumeEffect
import com.example.assetbookingmanagement.R
import com.example.assetbookingmanagement.core.ui.theme.*

private data class HomeCardUiModel(
    val backgroundColor: Color,
    val iconRes: Int,
    val primaryColor: Color,
    val count: String,
    val label: String,
    val arrowContentDescription: String
)

@Composable
fun HomeScreen(
    onAssetsClick: () -> Unit = {},
    onBookingsClick: () -> Unit = {},
    onApprovalRequestsClick: () -> Unit = {},
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val assetsCard = HomeCardUiModel(
        backgroundColor = AssetsCardBg,
        iconRes = R.drawable.computer_24,
        primaryColor = PrimaryBlue,
        count = uiState.assetCount.toString(),
        label = stringResource(R.string.home_all_assets_label),
        arrowContentDescription = stringResource(R.string.home_open_all_assets_content_description)
    )
    val bookingsCard = HomeCardUiModel(
        backgroundColor = BookingsCardBg,
        iconRes = R.drawable.calendar_today_24,
        primaryColor = BookingsPrimary,
        count = uiState.myBookingsCount.toString(),
        label = stringResource(R.string.home_my_bookings_label),
        arrowContentDescription = stringResource(R.string.home_open_my_bookings_content_description)
    )

    LifecycleResumeEffect(Unit) {
        viewModel.refreshHomeData()

        onPauseOrDispose { }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            HomeCard(
                modifier = Modifier.weight(1f),
                card = assetsCard,
                onArrowClick = onAssetsClick
            )

            HomeCard(
                modifier = Modifier.weight(1f),
                card = bookingsCard,
                onArrowClick = onBookingsClick
            )
        }

        if (uiState.canManageApprovals) {
            ApprovalRequestsCard(
                pendingCount = uiState.pendingApprovalsCount,
                onArrowClick = onApprovalRequestsClick
            )
        }
    }
}

@Composable
private fun HomeCard(
    modifier: Modifier = Modifier,
    card: HomeCardUiModel,
    onArrowClick: () -> Unit
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        color = card.backgroundColor
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Icon(
                painter = painterResource(id = card.iconRes),
                contentDescription = card.label,
                tint = card.primaryColor,
                modifier = Modifier.size(24.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(
                        text = card.count,
                        color = card.primaryColor,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = card.label,
                        color = TextLight,
                        fontSize = 12.sp
                    )
                }

                IconButton(
                    onClick = onArrowClick,
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.arrow_right_alt_24),
                        contentDescription = card.arrowContentDescription,
                        tint = TextLight,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun ApprovalRequestsCard(
    pendingCount: Int,
    onArrowClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 18.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = stringResource(R.string.home_pending_approvals_title),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = pluralStringResource(R.plurals.home_pending_approvals_requests, pendingCount, pendingCount),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            IconButton(
                onClick = onArrowClick,
                modifier = Modifier.size(28.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.arrow_right_alt_24),
                    contentDescription = stringResource(R.string.home_open_pending_approvals_content_description),
                    tint = TextLight,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
