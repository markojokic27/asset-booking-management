package com.example.assetbookingmanagement.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.assetbookingmanagement.features.auth.ui.LoginScreen
import com.example.assetbookingmanagement.features.home.ui.HomeScreen

@Composable
fun NavGraph(isUserLoggedIn: Boolean = false) {
    val navController = rememberNavController()
    // Select the start screen based on whether the user is logged in
    val startDestination = if (isUserLoggedIn) Routes.HOME else Routes.LOGIN

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Routes.LOGIN) {
            LoginScreen(
                onLoginSuccess = {
                    // After login, open Home
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.HOME) {
            HomeScreen()
        }
    }
}
