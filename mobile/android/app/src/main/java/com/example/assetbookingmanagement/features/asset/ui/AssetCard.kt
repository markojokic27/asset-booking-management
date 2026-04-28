package com.example.assetbookingmanagement.features.asset.ui

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
import com.example.assetbookingmanagement.core.ui.theme.AssetStatusActiveBg
import com.example.assetbookingmanagement.core.ui.theme.AssetStatusDamagedBg
import com.example.assetbookingmanagement.core.ui.theme.AssetStatusDeletedBg
import com.example.assetbookingmanagement.core.ui.theme.AssetStatusInactiveBg
import com.example.assetbookingmanagement.core.ui.theme.Gray100
import com.example.assetbookingmanagement.core.ui.theme.TextLight
import com.example.assetbookingmanagement.features.asset.data.AssetResponse

@Composable
fun AssetCard(
    asset: AssetResponse,
    onClick: () -> Unit = {}
) {
    Card(
        onClick = onClick,
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
                    text = asset.name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = asset.code,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = asset.location,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.55f)
                )
            }

            StatusBadge(status = asset.status)
        }
    }
}

private fun statusColor(status: String) = when (status.uppercase()) {
    "ACTIVE" -> AssetStatusActiveBg
    "INACTIVE" -> AssetStatusInactiveBg
    "DAMAGED" -> AssetStatusDamagedBg
    "DELETED" -> AssetStatusDeletedBg
    else -> Gray100
}

@Composable
private fun StatusBadge(status: String) {
    Surface(
        color = statusColor(status),
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
