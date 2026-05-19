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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.assetbookingmanagement.R
import com.example.assetbookingmanagement.core.ui.theme.*

@Composable
fun HomeScreen(
    onAssetsClick: () -> Unit = {},
    onBookingsClick: () -> Unit = {},
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        HomeCard(
            modifier = Modifier.weight(1f),
            backgroundColor = AssetsCardBg,
            iconRes = R.drawable.computer_24,
            primaryColor = PrimaryBlue,
            count = uiState.assetCount.toString(),
            label = "All assets",
            onArrowClick = onAssetsClick
        )

        HomeCard(
            modifier = Modifier.weight(1f),
            backgroundColor = BookingsCardBg,
            iconRes = R.drawable.calendar_today_24,
            primaryColor = BookingsPrimary,
            count = uiState.myBookingsCount.toString(),
            label = "My bookings",
            onArrowClick = onBookingsClick
        )
    }
}

@Composable
private fun HomeCard(
    modifier: Modifier = Modifier,
    backgroundColor: Color,
    iconRes: Int,
    primaryColor: Color,
    count: String,
    label: String,
    onArrowClick: () -> Unit
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        color = backgroundColor
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = label,
                tint = primaryColor,
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
                        text = count,
                        color = primaryColor,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = label,
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
                        contentDescription = "Open $label",
                        tint = TextLight,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}
