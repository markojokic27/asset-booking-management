package com.example.assetbookingmanagement.features.user.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.assetbookingmanagement.core.ui.components.AppButton
import com.example.assetbookingmanagement.core.ui.theme.InputFocusBorder

@Composable
fun ChangePasswordScreen(
    onCancelClick: () -> Unit = {},
    onPasswordChanged: () -> Unit = {},
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.prepareChangePassword()
    }

    LaunchedEffect(uiState.isPasswordChanged) {
        if (uiState.isPasswordChanged) {
            onPasswordChanged()
            viewModel.clearChangePasswordState()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 24.dp)
    ) {
        PasswordFieldSection(
            label = "Current password",
            value = uiState.currentPassword,
            enabled = !uiState.isChangingPassword,
            error = uiState.currentPasswordError,
            onValueChange = viewModel::onCurrentPasswordChange
        )
        Spacer(modifier = Modifier.height(16.dp))
        PasswordFieldSection(
            label = "New password",
            value = uiState.newPassword,
            enabled = !uiState.isChangingPassword,
            error = uiState.newPasswordError,
            onValueChange = viewModel::onNewPasswordChange
        )
        Spacer(modifier = Modifier.height(16.dp))
        PasswordFieldSection(
            label = "Confirm new password",
            value = uiState.confirmNewPassword,
            enabled = !uiState.isChangingPassword,
            error = uiState.confirmNewPasswordError,
            onValueChange = viewModel::onConfirmNewPasswordChange
        )

        uiState.changePasswordErrorMessage?.let { message ->
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = message,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Button(
                onClick = {
                    viewModel.clearChangePasswordState()
                    onCancelClick()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.28f),
                    contentColor = MaterialTheme.colorScheme.onSurface
                )
            ) {
                Text("Cancel", fontWeight = FontWeight.Bold)
            }

            AppButton(
                text = if (uiState.isChangingPassword) "Saving..." else "Save",
                enabled = !uiState.isChangingPassword,
                onClick = viewModel::changePassword
            )
        }
    }
}

@Composable
private fun PasswordFieldSection(
    label: String,
    value: String,
    enabled: Boolean,
    error: String?,
    onValueChange: (String) -> Unit
) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(8.dp))
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            enabled = enabled,
            singleLine = true,
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onBackground
            ),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.onBackground),
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            decorationBox = { innerTextField ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .background(
                            color = MaterialTheme.colorScheme.surface,
                            shape = RoundedCornerShape(10.dp)
                        )
                        .border(
                            width = 1.dp,
                            color = InputFocusBorder.copy(alpha = 0.12f),
                            shape = RoundedCornerShape(10.dp)
                        )
                        .padding(horizontal = 14.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    innerTextField()
                }
            }
        )
        error?.let {
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
