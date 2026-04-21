package com.example.assetbookingmanagement.app.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.assetbookingmanagement.core.ui.components.Header
import com.example.assetbookingmanagement.features.asset.ui.AssetsScreen
import com.example.assetbookingmanagement.features.auth.ui.LoginScreen
import com.example.assetbookingmanagement.features.booking.ui.BookingsScreen
import com.example.assetbookingmanagement.features.home.ui.HomeScreen
import com.example.assetbookingmanagement.features.user.ui.ProfileScreen

@Composable
fun NavGraph(isUserLoggedIn: Boolean = false) {
    val navController = rememberNavController()
    val startDestination = if (isUserLoggedIn) Routes.HOME else Routes.LOGIN
    val navBackStackEntry = navController.currentBackStackEntryAsState().value
    val currentRoute = navBackStackEntry?.destination?.route
    val showBottomBar = isBottomNavRoute(currentRoute)

    val headerTitle = when (currentRoute) {
        Routes.HOME -> "Home"
        Routes.ASSETS -> "Assets"
        Routes.BOOKINGS -> "Bookings"
        Routes.PROFILE -> "Profile"
        else -> ""
    }

    Scaffold(
        topBar = {
            if (showBottomBar) {
                Header(
                    title = headerTitle,
                    onNotificationClick = {
                    }
                )
            }
        },
        bottomBar = {
            if (showBottomBar) {
                BottomNavigationBar(navController = navController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Routes.LOGIN) {
                LoginScreen(
                    onLoginSuccess = {
                        navController.navigate(Routes.HOME) {
                            popUpTo(Routes.LOGIN) { inclusive = true }
                        }
                    }
                )
            }

            composable(Routes.HOME) {
                HomeScreen()
            }

            composable(Routes.ASSETS) {
                AssetsScreen()
            }

            composable(Routes.BOOKINGS) {
                BookingsScreen()
            }

            composable(Routes.PROFILE) {
                ProfileScreen()
            }
        }
    }
}