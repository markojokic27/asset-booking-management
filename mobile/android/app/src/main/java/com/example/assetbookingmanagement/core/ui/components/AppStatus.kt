package com.example.assetbookingmanagement.core.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.assetbookingmanagement.R
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
        text = localizedStatusText(status),
        statusStyle = statusBadgeStyle(status),
        modifier = modifier
    )
}

@Composable
fun RoleBadge(
    role: String,
    modifier: Modifier = Modifier
) {
    AppStatus(
        text = role,
        statusStyle = roleBadgeStyle(role),
        modifier = modifier
    )
}

@Composable
private fun statusBadgeStyle(status: String): AppStatusStyle {
    val isDark = isSystemInDarkTheme()
    return when (status.uppercase()) {
        "ACTIVE", "APPROVED" -> themedStatusStyle(
            isDark = isDark,
            darkStyle = AppStatusStyle(StatusActiveBgDark, StatusActiveTextDark, StatusActiveBorderDark),
            lightStyle = AppStatusStyle(StatusActiveBgLight, StatusActiveTextLight, StatusActiveBorderLight)
        )

        "INACTIVE", "REJECTED", "CANCELLED" -> themedStatusStyle(
            isDark = isDark,
            darkStyle = AppStatusStyle(StatusInactiveBgDark, StatusInactiveTextDark, StatusInactiveBorderDark),
            lightStyle = AppStatusStyle(StatusInactiveBgLight, StatusInactiveTextLight, StatusInactiveBorderLight)
        )

        "DAMAGED", "PENDING" -> themedStatusStyle(
            isDark = isDark,
            darkStyle = AppStatusStyle(StatusDamagedBgDark, StatusDamagedTextDark, StatusDamagedBorderDark),
            lightStyle = AppStatusStyle(StatusDamagedBgLight, StatusDamagedTextLight, StatusDamagedBorderLight)
        )

        "DELETED" -> themedStatusStyle(
            isDark = isDark,
            darkStyle = AppStatusStyle(StatusDeletedBgDark, StatusDeletedTextDark, StatusDeletedBorderDark),
            lightStyle = AppStatusStyle(StatusDeletedBgLight, StatusDeletedTextLight, StatusDeletedBorderLight)
        )

        "COMPLETED" -> themedStatusStyle(
            isDark = isDark,
            darkStyle = AppStatusStyle(StatusCompletedBgDark, StatusCompletedTextDark, StatusCompletedBorderDark),
            lightStyle = AppStatusStyle(StatusCompletedBgLight, StatusCompletedTextLight, StatusCompletedBorderLight)
        )

        else -> themedStatusStyle(
            isDark = isDark,
            darkStyle = AppStatusStyle(StatusNeutralBgDark, StatusNeutralTextDark, StatusNeutralBorderDark),
            lightStyle = AppStatusStyle(StatusNeutralBgLight, StatusNeutralTextLight, StatusNeutralBorderLight)
        )
    }
}

private fun themedStatusStyle(
    isDark: Boolean,
    darkStyle: AppStatusStyle,
    lightStyle: AppStatusStyle
): AppStatusStyle = if (isDark) darkStyle else lightStyle

@Composable
private fun localizedStatusText(status: String): String = when (status.uppercase()) {
    "ACTIVE" -> stringResource(R.string.status_active)
    "INACTIVE" -> stringResource(R.string.status_inactive)
    "DAMAGED" -> stringResource(R.string.status_damaged)
    "DELETED" -> stringResource(R.string.status_deleted)
    "APPROVED" -> stringResource(R.string.status_approved)
    "REJECTED" -> stringResource(R.string.status_rejected)
    "CANCELLED" -> stringResource(R.string.status_cancelled)
    "PENDING" -> stringResource(R.string.status_pending)
    "COMPLETED" -> stringResource(R.string.status_completed)
    else -> status
}

@Composable
private fun roleBadgeStyle(role: String): AppStatusStyle {
    val isDark = isSystemInDarkTheme()
    return when (role.uppercase()) {
        "ADMIN" -> if (isDark) {
            AppStatusStyle(RoleAdminBgDark, RoleAdminTextDark, RoleAdminBorderDark)
        } else {
            AppStatusStyle(RoleAdminBgLight, RoleAdminTextLight, RoleAdminBorderLight)
        }

        "MANAGER" -> if (isDark) {
            AppStatusStyle(RoleManagerBgDark, RoleManagerTextDark, RoleManagerBorderDark)
        } else {
            AppStatusStyle(RoleManagerBgLight, RoleManagerTextLight, RoleManagerBorderLight)
        }

        else -> if (isDark) {
            AppStatusStyle(RoleEmployeeBgDark, RoleEmployeeTextDark, RoleEmployeeBorderDark)
        } else {
            AppStatusStyle(RoleEmployeeBgLight, RoleEmployeeTextLight, RoleEmployeeBorderLight)
        }
    }
}
