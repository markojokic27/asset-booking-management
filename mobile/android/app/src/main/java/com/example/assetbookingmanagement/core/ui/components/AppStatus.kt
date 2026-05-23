package com.example.assetbookingmanagement.core.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.assetbookingmanagement.core.ui.theme.*

data class AppStatusStyle(
    val background: Color,
    val text: Color,
    val border: Color
)

@Composable
fun AppStatus(
    text: String,
    statusStyle: AppStatusStyle,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(999.dp),
        color = statusStyle.background,
        border = BorderStroke(1.dp, statusStyle.border)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold,
            color = statusStyle.text
        )
    }
}

@Composable
fun StatusBadge(
    status: String,
    modifier: Modifier = Modifier
) {
    AppStatus(
        text = status,
        statusStyle = statusBadgeStyle(status),
        modifier = modifier
    )
}

@Composable
private fun statusBadgeStyle(status: String): AppStatusStyle {
    val isDark = MaterialTheme.colorScheme.background == BgDark
    return when (status.uppercase()) {
        "ACTIVE", "APPROVED" -> if (isDark) {
            AppStatusStyle(StatusActiveBgDark, StatusActiveTextDark, StatusActiveBorderDark)
        } else {
            AppStatusStyle(StatusActiveBgLight, StatusActiveTextLight, StatusActiveBorderLight)
        }

        "INACTIVE", "REJECTED", "CANCELLED" -> if (isDark) {
            AppStatusStyle(StatusInactiveBgDark, StatusInactiveTextDark, StatusInactiveBorderDark)
        } else {
            AppStatusStyle(StatusInactiveBgLight, StatusInactiveTextLight, StatusInactiveBorderLight)
        }

        "DAMAGED", "PENDING" -> if (isDark) {
            AppStatusStyle(StatusDamagedBgDark, StatusDamagedTextDark, StatusDamagedBorderDark)
        } else {
            AppStatusStyle(StatusDamagedBgLight, StatusDamagedTextLight, StatusDamagedBorderLight)
        }

        "DELETED" -> if (isDark) {
            AppStatusStyle(StatusDeletedBgDark, StatusDeletedTextDark, StatusDeletedBorderDark)
        } else {
            AppStatusStyle(StatusDeletedBgLight, StatusDeletedTextLight, StatusDeletedBorderLight)
        }

        "COMPLETED" -> if (isDark) {
            AppStatusStyle(StatusCompletedBgDark, StatusCompletedTextDark, StatusCompletedBorderDark)
        } else {
            AppStatusStyle(StatusCompletedBgLight, StatusCompletedTextLight, StatusCompletedBorderLight)
        }

        else -> if (isDark) {
            AppStatusStyle(StatusNeutralBgDark, StatusNeutralTextDark, StatusNeutralBorderDark)
        } else {
            AppStatusStyle(StatusNeutralBgLight, StatusNeutralTextLight, StatusNeutralBorderLight)
        }
    }
}
