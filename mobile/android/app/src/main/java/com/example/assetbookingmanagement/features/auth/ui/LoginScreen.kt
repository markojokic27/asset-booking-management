package com.example.assetbookingmanagement.features.auth.ui

import android.content.res.Configuration
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.assetbookingmanagement.R
import com.example.assetbookingmanagement.core.ui.components.*

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit = {},
    viewModel: LoginViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    val isDarkTheme = isSystemInDarkTheme()

    LaunchedEffect(uiState.isLoggedIn) {
        if (uiState.isLoggedIn) {
            onLoginSuccess()
        }
    }

    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE
    val spacing = if (isLandscape) 8.dp else 16.dp

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LoginHeader(isLandscape = isLandscape, isDarkTheme = isDarkTheme)

        Spacer(modifier = Modifier.height(if (isLandscape) 10.dp else 40.dp))

        LoginCard {
            Text(
                text = stringResource(R.string.login_title),
                fontSize = if (isLandscape) 24.sp else 32.sp,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.align(Alignment.CenterHorizontally).padding(bottom = spacing)
            )

            LabeledInput(
                label = stringResource(R.string.login_field_username),
                value = username,
                placeholder = stringResource(R.string.login_placeholder_username),
                isLandscape = isLandscape
            ) { username = it }
            Spacer(modifier = Modifier.height(spacing))
            LabeledInput(
                label = stringResource(R.string.login_field_password),
                value = password,
                placeholder = stringResource(R.string.login_placeholder_password),
                isLandscape = isLandscape,
                isPassword = true,
                passwordVisible = passwordVisible,
                passwordVisibilityToggle = { passwordVisible = !passwordVisible }
            ) { password = it }

            Spacer(modifier = Modifier.height(spacing * 1.5f))

            uiState.errorMessageRes?.let {
                Text(
                    text = stringResource(it),
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 12.sp
                )
            }

            AppButton(
                text = if (uiState.isLoading) {
                    stringResource(R.string.login_loading)
                } else {
                    stringResource(R.string.login_submit)
                },
                enabled = !uiState.isLoading,
                onClick = { viewModel.login(username, password) }
            )
        }
    }
}

@Composable
fun LoginHeader(isLandscape: Boolean, isDarkTheme: Boolean) {
    val logoColorFilter =
        if (isDarkTheme) ColorFilter.tint(MaterialTheme.colorScheme.onBackground) else null

    if (isLandscape) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = null,
                modifier = Modifier.height(40.dp),
                colorFilter = logoColorFilter
            )
            Spacer(Modifier.width(12.dp))
            Text(
                text = stringResource(R.string.app_name),
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    } else {
        Spacer(Modifier.height(50.dp))
        Image(
            painter = painterResource(R.drawable.logo),
            contentDescription = null,
            modifier = Modifier.height(80.dp),
            colorFilter = logoColorFilter
        )
        Text(
            text = stringResource(R.string.app_name),
            fontSize = 17.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
fun LabeledInput(
    label: String,
    value: String,
    placeholder: String,
    isLandscape: Boolean,
    isPassword: Boolean = false,
    passwordVisible: Boolean = false,
    passwordVisibilityToggle: (() -> Unit)? = null,
    onValueChange: (String) -> Unit
) {
    Text(
        text = label,
        fontSize = 13.sp,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.padding(bottom = if (isLandscape) 2.dp else 6.dp)
    )
    AppInput(
        value = value,
        onValueChange = onValueChange,
        placeholder = placeholder,
        visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(keyboardType = if (isPassword) KeyboardType.Password else KeyboardType.Text),
        passwordVisibilityToggle = if (isPassword && passwordVisibilityToggle != null) {
            {
                IconButton(onClick = passwordVisibilityToggle) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                        contentDescription = if (passwordVisible) {
                            stringResource(R.string.login_hide_password)
                        } else {
                            stringResource(R.string.login_show_password)
                        }
                    )
                }
            }
        } else {
            null
        }
    )
}

@Composable
fun LoginCard(content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        content = { Column(Modifier.padding(20.dp), content = content) }
    )
}