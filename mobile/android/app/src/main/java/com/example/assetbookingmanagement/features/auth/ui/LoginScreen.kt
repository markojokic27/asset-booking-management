package com.example.assetbookingmanagement.features.auth.ui

import android.content.res.Configuration
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
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
        LoginHeader(isLandscape)

        Spacer(modifier = Modifier.height(if (isLandscape) 10.dp else 40.dp))

        LoginCard {
            Text(
                text = "Login",
                fontSize = if (isLandscape) 24.sp else 32.sp,
                fontWeight = FontWeight.Black,
                modifier = Modifier.align(Alignment.CenterHorizontally).padding(bottom = spacing)
            )

            LabeledInput("Username", username, "Enter username", isLandscape) { username = it }
            Spacer(modifier = Modifier.height(spacing))
            LabeledInput("Password", password, "Enter password", isLandscape, true) { password = it }

            Spacer(modifier = Modifier.height(spacing * 1.5f))

            uiState.errorMessage?.let {
                Text(it, color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
            }

            AppButton(
                text = if (uiState.isLoading) "LOGGING IN..." else "LOGIN",
                enabled = !uiState.isLoading,
                onClick = { viewModel.login(username, password) }
            )
        }
    }
}

@Composable
fun LoginHeader(isLandscape: Boolean) {
    if (isLandscape) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(painterResource(R.drawable.logo), null, Modifier.height(40.dp))
            Spacer(Modifier.width(12.dp))
            Text("Asset Booking Management", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        }
    } else {
        Spacer(Modifier.height(50.dp))
        Image(painterResource(R.drawable.logo), null, Modifier.height(80.dp))
        Text("Asset Booking Management", fontSize = 17.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
fun LabeledInput(label: String, value: String, placeholder: String, isLandscape: Boolean, isPassword: Boolean = false, onValueChange: (String) -> Unit) {
    Text(label, fontSize = 13.sp, modifier = Modifier.padding(bottom = if (isLandscape) 2.dp else 6.dp))
    AppInput(
        value = value,
        onValueChange = onValueChange,
        placeholder = placeholder,
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(keyboardType = if (isPassword) KeyboardType.Password else KeyboardType.Text)
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