package com.example.assetbookingmanagement.app

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.assetbookingmanagement.app.navigation.NavGraph
import com.example.assetbookingmanagement.core.ui.components.AppLoadingState

@Composable
fun App(
    viewModel: AppViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    if (uiState.isLoading) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                AppLoadingState()
            }
        }
    } else {
        NavGraph(
            isUserLoggedIn = uiState.isUserLoggedIn,
            onUserLoggedOut = { viewModel.onUserLoggedOut() }
        )
    }
}
