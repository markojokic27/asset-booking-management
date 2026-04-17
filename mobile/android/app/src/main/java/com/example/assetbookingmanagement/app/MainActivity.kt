package com.example.assetbookingmanagement.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.assetbookingmanagement.core.ui.theme.AssetBookingManagementTheme
import dagger.hilt.android.AndroidEntryPoint

// Allows Hilt to provide dependencies to this Activity and its Compose screens
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AssetBookingManagementTheme {
                App()
            }
        }
    }
}
