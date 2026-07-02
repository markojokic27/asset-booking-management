package com.example.assetbookingmanagement.app.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavType
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NamedNavArgument
import androidx.navigation.navArgument
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.assetbookingmanagement.R
import com.example.assetbookingmanagement.core.ui.components.Header
import com.example.assetbookingmanagement.features.asset.ui.AssetDetailsScreen
import com.example.assetbookingmanagement.features.asset.ui.AssetsScreen
import com.example.assetbookingmanagement.features.auth.ui.LoginScreen
import com.example.assetbookingmanagement.features.booking.ui.ApprovalRequestDetailsScreen
import com.example.assetbookingmanagement.features.booking.ui.ApprovalRequestDetailsUiModel
import com.example.assetbookingmanagement.features.booking.ui.BookingDetailsScreen
import com.example.assetbookingmanagement.features.booking.ui.BookingSuccessScreen
import com.example.assetbookingmanagement.features.booking.ui.BookingsScreen
import com.example.assetbookingmanagement.features.booking.ui.ApprovalRequestsScreen
import com.example.assetbookingmanagement.features.home.ui.HomeScreen
import com.example.assetbookingmanagement.features.user.ui.ChangePasswordScreen
import com.example.assetbookingmanagement.features.user.ui.ProfileScreen
import com.example.assetbookingmanagement.features.booking.ui.CreateBookingScreen

@Composable
fun NavGraph(
    isUserLoggedIn: Boolean = false,
    onUserLoggedOut: () -> Unit = {}
) {
    key(isUserLoggedIn) {
        val navController = rememberNavController()
        val startDestination = if (isUserLoggedIn) Routes.HOME else Routes.LOGIN
        val navBackStackEntry = navController.currentBackStackEntryAsState().value
        val currentRoute = navBackStackEntry?.destination?.route
        val currentBookingId = navBackStackEntry?.arguments?.getLong("bookingId")
        val showBottomBar =
            isBottomNavRoute(currentRoute) ||
                currentRoute == Routes.ASSET_DETAILS ||
                currentRoute == Routes.BOOKING_DETAILS ||
                currentRoute == Routes.APPROVAL_REQUESTS ||
                currentRoute == Routes.APPROVAL_REQUEST_DETAILS ||
                currentRoute == Routes.CREATE_BOOKING ||
                currentRoute == Routes.BOOKING_SUCCESS ||
                currentRoute == Routes.CHANGE_PASSWORD

        val headerTitle = when (currentRoute) {
            Routes.HOME -> stringResource(R.string.nav_home_label)
            Routes.ASSETS -> stringResource(R.string.nav_assets_label)
            Routes.BOOKINGS -> stringResource(R.string.nav_bookings_label)
            Routes.APPROVAL_REQUESTS -> stringResource(R.string.nav_approvals_title)
            Routes.APPROVAL_REQUEST_DETAILS -> currentBookingId?.let {
                stringResource(R.string.nav_approval_request_details_title, it)
            } ?: stringResource(R.string.nav_booking_label)
            Routes.BOOKING_DETAILS -> stringResource(R.string.nav_booking_details_title)
            Routes.PROFILE -> stringResource(R.string.nav_profile_label)
            Routes.CHANGE_PASSWORD -> stringResource(R.string.nav_change_password_title)
            Routes.CREATE_BOOKING -> stringResource(R.string.nav_create_booking_title)
            Routes.BOOKING_SUCCESS -> stringResource(R.string.nav_booking_status_title)
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
                    },
                    onApprovalRequestsClick = {
                        navController.navigate(Routes.APPROVAL_REQUESTS)
                    }
                )
            }

            composable(Routes.APPROVAL_REQUESTS) {
                ApprovalRequestsScreen(
                    onApprovalRequestClick = { request ->
                        navController.navigate(
                            Routes.approvalRequestDetails(
                                bookingId = request.id,
                                assetName = request.assetName,
                                requesterName = request.requesterName,
                                fromDate = request.bookingStart,
                                toDate = request.bookingEnd,
                                status = request.status,
                                isHourlyBooking = request.isHourlyBooking
                            )
                        )
                    }
                )
            }

            composable(
                route = Routes.APPROVAL_REQUEST_DETAILS,
                arguments = approvalRequestDetailsArguments
            ) { backStackEntry ->
                val bookingId = backStackEntry.arguments?.getLong("bookingId") ?: return@composable
                ApprovalRequestDetailsScreen(
                    details = ApprovalRequestDetailsUiModel(
                        bookingId = bookingId,
                        assetName = backStackEntry.stringArg("assetName"),
                        requesterName = backStackEntry.stringArg("requesterName"),
                        bookingStart = backStackEntry.stringArg("fromDate", "-"),
                        bookingEnd = backStackEntry.stringArg("toDate", "-"),
                        status = backStackEntry.stringArg("status", "-"),
                        isHourlyBooking = backStackEntry.boolArg("isHourlyBooking")
                    ),
                    onApproved = {
                        navController.popBackStack()
                    },
                    onRejected = {
                        navController.popBackStack()
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
                arguments = bookingDetailsArguments
            ) { backStackEntry ->
                val bookingId = backStackEntry.arguments?.getLong("bookingId") ?: return@composable
                BookingDetailsScreen(
                    bookingId = bookingId,
                    assetName = backStackEntry.stringArg("assetName"),
                    bookingStart = backStackEntry.stringArg("fromDate", "-"),
                    bookingEnd = backStackEntry.stringArg("toDate", "-"),
                    status = backStackEntry.stringArg("status", "-"),
                    categoryName = backStackEntry.stringArg("categoryName", "-"),
                    isHourlyBooking = backStackEntry.boolArg("isHourlyBooking"),
                    onCancelled = {
                        navController.popBackStack()
                    }
                )
            }

            composable(Routes.PROFILE) {
                ProfileScreen(
                    onChangePasswordClick = {
                        navController.navigate(Routes.CHANGE_PASSWORD)
                    },
                    onLogoutSuccess = {
                        navController.navigate(Routes.LOGIN) {
                            popUpTo(navController.graph.id) { 
                                inclusive = true
                                saveState = false
                            }
                        }
                        onUserLoggedOut()
                    }
                )
            }

            composable(Routes.CHANGE_PASSWORD) {
                ChangePasswordScreen(
                    onCancelClick = {
                        navController.popBackStack()
                    },
                    onPasswordChanged = {
                        navController.popBackStack()
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
                    onBookNowClick = { assetName, fromDate, toDate, approvalRequired ->
                        navController.navigate(
                            Routes.bookingSuccess(
                                assetName = assetName,
                                fromDate = fromDate,
                                toDate = toDate,
                                approvalRequired = approvalRequired
                            )
                        ) {
                            popUpTo(Routes.CREATE_BOOKING) { inclusive = true }
                        }
                    }
                )
            }

            composable(
                route = Routes.BOOKING_SUCCESS,
                arguments = bookingSuccessArguments
            ) { backStackEntry ->
                BookingSuccessScreen(
                    assetName = backStackEntry.stringArg("assetName"),
                    fromDate = backStackEntry.stringArg("fromDate", "-"),
                    toDate = backStackEntry.stringArg("toDate", "-"),
                    showApprovalMessage = backStackEntry.boolArg("approvalRequired")
                )
            }
            }
        }
    }
}

private fun longNavArg(name: String): NamedNavArgument =
    navArgument(name) { type = NavType.LongType }

private fun stringNavArg(
    name: String,
    defaultValue: String = ""
): NamedNavArgument =
    navArgument(name) {
        type = NavType.StringType
        this.defaultValue = defaultValue
    }

private fun boolNavArg(
    name: String,
    defaultValue: Boolean = false
): NamedNavArgument =
    navArgument(name) {
        type = NavType.BoolType
        this.defaultValue = defaultValue
    }

private val bookingBaseArguments = listOf(
    longNavArg("bookingId"),
    stringNavArg("assetName"),
    stringNavArg("fromDate", "-"),
    stringNavArg("toDate", "-"),
    stringNavArg("status", "-"),
    boolNavArg("isHourlyBooking")
)

private val approvalRequestDetailsArguments = bookingBaseArguments + listOf(
    stringNavArg("requesterName")
)

private val bookingDetailsArguments = bookingBaseArguments + listOf(
    stringNavArg("categoryName", "-")
)

private val bookingSuccessArguments = listOf(
    stringNavArg("assetName"),
    stringNavArg("fromDate", "-"),
    stringNavArg("toDate", "-"),
    boolNavArg("approvalRequired")
)

private fun NavBackStackEntry.stringArg(
    name: String,
    defaultValue: String = ""
): String = arguments?.getString(name) ?: defaultValue

private fun NavBackStackEntry.boolArg(name: String): Boolean =
    arguments?.getBoolean(name) ?: false
