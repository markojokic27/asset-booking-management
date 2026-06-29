package com.example.assetbookingmanagement.features.user.ui

import androidx.compose.ui.res.stringResource
import com.example.assetbookingmanagement.R
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
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
import com.example.assetbookingmanagement.core.ui.components.AppLoadingState
import com.example.assetbookingmanagement.core.ui.components.AppMessageState
import com.example.assetbookingmanagement.core.ui.components.DetailsRow
import com.example.assetbookingmanagement.core.ui.components.DetailsSectionCard
import com.example.assetbookingmanagement.core.ui.components.RoleBadge
import com.example.assetbookingmanagement.core.ui.components.StatusBadge
import com.example.assetbookingmanagement.features.user.data.UserResponse

@Composable
fun ProfileScreen(
    onLogoutSuccess: () -> Unit = {},
    onChangePasswordClick: () -> Unit = {},
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val unavailableText = stringResource(R.string.common_value_unavailable)

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
                AppLoadingState()
            }

            uiState.errorMessageResId != null -> {
                val errorMessageResId = uiState.errorMessageResId ?: return@Surface
                AppMessageState(
                    title = stringResource(R.string.profile_error_load_title),
                    message = stringResource(errorMessageResId)
                )
            }

            else -> uiState.profile?.let { profile ->
                ProfileContent(
                    profile = profile,
                    departmentName = uiState.departmentName,
                    isLoggingOut = uiState.isLoggingOut,
                    unavailableText = unavailableText,
                    onChangePasswordClick = onChangePasswordClick,
                    onLogoutClick = viewModel::logout
                )
            }
        }
    }
}

@Composable
private fun ProfileContent(
    profile: UserResponse,
    departmentName: String,
    isLoggingOut: Boolean,
    unavailableText: String,
    onChangePasswordClick: () -> Unit,
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
                unavailableText = unavailableText,
                onChangePasswordClick = onChangePasswordClick,
                onLogoutClick = onLogoutClick
            )
        }

        item {
            WorkDetailsSection(
                profile = profile,
                departmentName = departmentName,
                unavailableText = unavailableText
            )
        }
    }
}

@Composable
private fun ProfileDetailsSection(
    profile: UserResponse,
    isLoggingOut: Boolean,
    unavailableText: String,
    onChangePasswordClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    DetailsSectionCard(
        title = stringResource(R.string.profile_details_section_title),
        heading = listOf(profile.name, profile.surname)
            .filter { it.isNotBlank() }
            .joinToString(" ")
            .ifBlank { stringResource(R.string.nav_profile_label) },
        subtitle = profile.email
    ) {
        InfoRow(label = stringResource(R.string.profile_label_id), value = profile.id.toString(), showDivider = true)
        InfoRow(label = stringResource(R.string.profile_label_first_name), value = profile.name.ifBlank { unavailableText }, showDivider = true)
        InfoRow(label = stringResource(R.string.profile_label_last_name), value = profile.surname.ifBlank { unavailableText }, showDivider = true)
        InfoRow(label = stringResource(R.string.common_username), value = profile.username.ifBlank { unavailableText }, showDivider = true)
        InfoRow(label = stringResource(R.string.profile_label_email), value = profile.email.ifBlank { unavailableText }, showDivider = true)
        DetailsRow(showDivider = false) {
            Text(
                text = stringResource(R.string.login_field_password),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            OutlinedButton(
                onClick = onChangePasswordClick,
                modifier = Modifier.defaultMinSize(minHeight = 32.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface
                ),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = stringResource(R.string.nav_change_password_title),
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
private fun WorkDetailsSection(
    profile: UserResponse,
    departmentName: String,
    unavailableText: String
) {
    DetailsSectionCard(
        title = stringResource(R.string.profile_work_section_title),
        heading = stringResource(R.string.profile_details_section_title)
    ) {
        DetailsRow(showDivider = true) {
            Text(
                text = stringResource(R.string.profile_label_role),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            RoleBadge(role = profile.role.ifBlank { unavailableText })
        }
        DetailsRow(showDivider = true) {
            Text(
                text = stringResource(R.string.common_status),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            StatusBadge(status = profile.status.ifBlank { unavailableText })
        }
        InfoRow(
            label = stringResource(R.string.profile_label_department),
            value = departmentName.ifBlank { unavailableText },
            showDivider = true
        )
        InfoRow(
            label = stringResource(R.string.profile_label_manager_email),
            value = profile.managerEmail.ifBlank { unavailableText },
            showDivider = true
        )
        InfoRow(
            label = stringResource(R.string.profile_label_notes),
            value = profile.notes.orEmpty().ifBlank { unavailableText },
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
            contentDescription = stringResource(R.string.profile_logout),
            tint = logoutColor,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.size(8.dp))
        Text(
            text = if (isLoggingOut) {
                stringResource(R.string.profile_logging_out)
            } else {
                stringResource(R.string.profile_logout)
            },
            style = MaterialTheme.typography.bodyLarge,
            color = logoutColor,
            fontWeight = FontWeight.Medium
        )
    }
}
