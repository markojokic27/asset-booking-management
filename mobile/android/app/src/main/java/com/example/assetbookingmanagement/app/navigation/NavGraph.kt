package com.example.assetbookingmanagement.app.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.assetbookingmanagement.core.ui.components.Header
import com.example.assetbookingmanagement.features.asset.ui.AssetDetailsScreen
import com.example.assetbookingmanagement.features.asset.ui.AssetsScreen
import com.example.assetbookingmanagement.features.auth.ui.LoginScreen
import com.example.assetbookingmanagement.features.booking.ui.BookingDetailsScreen
import com.example.assetbookingmanagement.features.booking.ui.BookingSuccessScreen
import com.example.assetbookingmanagement.features.booking.ui.BookingsScreen
import com.example.assetbookingmanagement.features.home.ui.HomeScreen
import com.example.assetbookingmanagement.features.user.ui.ProfileScreen
import com.example.assetbookingmanagement.features.booking.ui.CreateBookingScreen

@Composable
fun NavGraph(isUserLoggedIn: Boolean = false) {
    val navController = rememberNavController()
    val startDestination = if (isUserLoggedIn) Routes.HOME else Routes.LOGIN
    val navBackStackEntry = navController.currentBackStackEntryAsState().value
    val currentRoute = navBackStackEntry?.destination?.route
    val showBottomBar =
        isBottomNavRoute(currentRoute) ||
            currentRoute == Routes.ASSET_DETAILS ||
            currentRoute == Routes.BOOKING_DETAILS ||
            currentRoute == Routes.CREATE_BOOKING ||
            currentRoute == Routes.BOOKING_SUCCESS

    val headerTitle = when (currentRoute) {
        Routes.HOME -> "Home"
        Routes.ASSETS -> "Assets"
        Routes.BOOKINGS -> "Bookings"
        Routes.BOOKING_DETAILS -> "Booking details"
        Routes.PROFILE -> "Profile"
        Routes.CREATE_BOOKING -> "Create booking"
        Routes.BOOKING_SUCCESS -> "Booking status"
        else -> ""
    }
    Scaffold(
        topBar = {
            if (showBottomBar) {
                Header(
                    title = headerTitle,
                    showBackArrow = currentRoute != Routes.HOME,
                    onBackClick = {
                        navController.popBackStack()
                    },
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
                HomeScreen(
                    onAssetsClick = {
                        navController.navigateTopLevel(Routes.ASSETS)
                    },
                    onBookingsClick = {
                        navController.navigateTopLevel(Routes.BOOKINGS)
                    }
                )
            }

            composable(Routes.ASSETS) {
                AssetsScreen(
                    onAssetClick = { assetId ->
                        navController.navigate(Routes.assetDetails(assetId))
                    }
                )
            }

            composable(
                route = Routes.ASSET_DETAILS,
                arguments = listOf(
                    navArgument("assetId") { type = NavType.LongType }
                )
            ) { backStackEntry ->
                val assetId = backStackEntry.arguments?.getLong("assetId") ?: return@composable
                AssetDetailsScreen(
                    assetId = assetId,
                    onBookClick = {
                        navController.navigate(Routes.createBooking(assetId))
                    }
                )
            }

            composable(Routes.BOOKINGS) {
                BookingsScreen(
                    onBookingClick = { booking ->
                        navController.navigate(
                            Routes.bookingDetails(
                                bookingId = booking.id,
                                assetName = booking.assetName,
                                fromDate = booking.bookingStart,
                                toDate = booking.bookingEnd,
                                status = booking.status,
                                categoryName = booking.categoryName,
                                isHourlyBooking = booking.isHourlyBooking
                            )
                        )
                    }
                )
            }

            composable(
                route = Routes.BOOKING_DETAILS,
                arguments = listOf(
                    navArgument("bookingId") { type = NavType.LongType },
                    navArgument("assetName") {
                        type = NavType.StringType
                        defaultValue = ""
                    },
                    navArgument("fromDate") {
                        type = NavType.StringType
                        defaultValue = "-"
                    },
                    navArgument("toDate") {
                        type = NavType.StringType
                        defaultValue = "-"
                    },
                    navArgument("status") {
                        type = NavType.StringType
                        defaultValue = "-"
                    },
                    navArgument("categoryName") {
                        type = NavType.StringType
                        defaultValue = "-"
                    },
                    navArgument("isHourlyBooking") {
                        type = NavType.BoolType
                        defaultValue = false
                    }
                )
            ) { backStackEntry ->
                val bookingId = backStackEntry.arguments?.getLong("bookingId") ?: return@composable
                BookingDetailsScreen(
                    bookingId = bookingId,
                    assetName = backStackEntry.arguments?.getString("assetName").orEmpty(),
                    bookingStart = backStackEntry.arguments?.getString("fromDate") ?: "-",
                    bookingEnd = backStackEntry.arguments?.getString("toDate") ?: "-",
                    status = backStackEntry.arguments?.getString("status") ?: "-",
                    categoryName = backStackEntry.arguments?.getString("categoryName") ?: "-",
                    isHourlyBooking = backStackEntry.arguments?.getBoolean("isHourlyBooking") ?: false
                )
            }

            composable(Routes.PROFILE) {
                ProfileScreen(
                    onLogoutSuccess = {
                        navController.navigate(Routes.LOGIN) {
                            popUpTo(navController.graph.id) { inclusive = true }
                        }
                    }
                )
            }
            composable(
                route = Routes.CREATE_BOOKING,
                arguments = listOf(
                    navArgument("assetId") { type = NavType.LongType }
                )
            ) { backStackEntry ->
                val assetId = backStackEntry.arguments?.getLong("assetId") ?: return@composable
                CreateBookingScreen(
                    assetId = assetId,
                    onCancelClick = {
                        navController.popBackStack()
                    },
                    onBookNowClick = { assetName, fromDate, toDate ->
                        navController.navigate(
                            Routes.bookingSuccess(
                                assetName = assetName,
                                fromDate = fromDate,
                                toDate = toDate
                            )
                        ) {
                            popUpTo(Routes.CREATE_BOOKING) { inclusive = true }
                        }
                    }
                )
            }

            composable(
                route = Routes.BOOKING_SUCCESS,
                arguments = listOf(
                    navArgument("assetName") {
                        type = NavType.StringType
                        defaultValue = ""
                    },
                    navArgument("fromDate") {
                        type = NavType.StringType
                        defaultValue = "-"
                    },
                    navArgument("toDate") {
                        type = NavType.StringType
                        defaultValue = "-"
                    }
                )
            ) { backStackEntry ->
                BookingSuccessScreen(
                    assetName = backStackEntry.arguments?.getString("assetName").orEmpty(),
                    fromDate = backStackEntry.arguments?.getString("fromDate") ?: "-",
                    toDate = backStackEntry.arguments?.getString("toDate") ?: "-"
                )
            }
        }
    }
}