package com.example.assetbookingmanagement.features.user.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.foundation.BorderStroke
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.foundation.interaction.MutableInteractionSource
import com.example.assetbookingmanagement.core.ui.components.DetailsRow
import com.example.assetbookingmanagement.core.ui.components.DetailsSectionCard
import com.example.assetbookingmanagement.core.ui.components.RoleBadge
import com.example.assetbookingmanagement.core.ui.components.StatusBadge
import com.example.assetbookingmanagement.features.user.data.UserResponse

@Composable
fun ProfileScreen(
    onLogoutSuccess: () -> Unit = {},
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.isLoggedOut) {
        if (uiState.isLoggedOut) {
            onLogoutSuccess()
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        when {
            uiState.isLoading -> {
                Text(
                    text = "Loading...",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            uiState.errorMessage != null -> {
                Text(
                    text = uiState.errorMessage.orEmpty(),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            else -> uiState.profile?.let { profile ->
                ProfileContent(
                    profile = profile,
                    isLoggingOut = uiState.isLoggingOut,
                    onLogoutClick = viewModel::logout
                )
            }
        }
    }
}

@Composable
private fun ProfileContent(
    profile: UserResponse,
    isLoggingOut: Boolean,
    onLogoutClick: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            ProfileDetailsSection(
                profile = profile,
                isLoggingOut = isLoggingOut,
                onLogoutClick = onLogoutClick
            )
        }

        item {
            WorkDetailsSection(profile = profile)
        }
    }
}

@Composable
private fun ProfileDetailsSection(
    profile: UserResponse,
    isLoggingOut: Boolean,
    onLogoutClick: () -> Unit
) {
    DetailsSectionCard(
        title = "PROFILE DETAILS",
        heading = listOf(profile.name, profile.surname)
            .filter { it.isNotBlank() }
            .joinToString(" "),
        subtitle = profile.email
    ) {
        InfoRow(label = "ID", value = profile.id.toString(), showDivider = true)
        InfoRow(label = "First name", value = profile.name.ifBlank { "-" }, showDivider = true)
        InfoRow(label = "Last name", value = profile.surname.ifBlank { "-" }, showDivider = true)
        InfoRow(label = "Username", value = profile.username.ifBlank { "-" }, showDivider = true)
        InfoRow(label = "Email", value = profile.email.ifBlank { "-" }, showDivider = true)
        DetailsRow(showDivider = false) {
            Text(
                text = "Password",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            OutlinedButton(
                onClick = {},
                modifier = Modifier.defaultMinSize(minHeight = 32.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface
                ),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "Change password",
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
        LogoutRow(
            isLoggingOut = isLoggingOut,
            onLogoutClick = onLogoutClick
        )
    }
}

@Composable
private fun WorkDetailsSection(profile: UserResponse) {
    DetailsSectionCard(
        title = "WORK DETAILS",
        heading = "Account details"
    ) {
        DetailsRow(showDivider = true) {
            Text(
                text = "Role",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            RoleBadge(role = profile.role.ifBlank { "-" })
        }
        DetailsRow(showDivider = true) {
            Text(
                text = "Status",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            StatusBadge(status = profile.status.ifBlank { "-" })
        }
        InfoRow(
            label = "Department",
            value = profile.departmentId.toString(),
            showDivider = true
        )
        InfoRow(
            label = "Manager email",
            value = profile.managerEmail.ifBlank { "-" },
            showDivider = true
        )
        InfoRow(
            label = "Notes",
            value = profile.notes.orEmpty().ifBlank { "-" },
            showDivider = false
        )
    }
}

@Composable
private fun InfoRow(
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
private fun LogoutRow(
    isLoggingOut: Boolean,
    onLogoutClick: () -> Unit
) {
    val logoutColor = MaterialTheme.colorScheme.error

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                enabled = !isLoggingOut,
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onLogoutClick
            )
            .padding(top = 16.dp, bottom = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Outlined.Logout,
            contentDescription = "Logout",
            tint = logoutColor,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.size(8.dp))
        Text(
            text = if (isLoggingOut) "Logging out..." else "Logout",
            style = MaterialTheme.typography.bodyLarge,
            color = logoutColor,
            fontWeight = FontWeight.Medium
        )
    }
}
