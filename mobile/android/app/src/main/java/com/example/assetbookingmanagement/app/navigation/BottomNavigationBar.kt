package com.example.assetbookingmanagement.app.navigation

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.assetbookingmanagement.R
import com.example.assetbookingmanagement.core.ui.theme.PrimaryBlue

private data class BottomNavItem(
    val route: String,
    val labelResId: Int,
    @param:DrawableRes val iconRes: Int
)

private val bottomNavItems = listOf(
    BottomNavItem(
        route = Routes.HOME,
        labelResId = R.string.nav_home_label,
        iconRes = R.drawable.home_24
    ),
    BottomNavItem(
        route = Routes.ASSETS,
        labelResId = R.string.nav_assets_label,
        iconRes = R.drawable.computer_24
    ),
    BottomNavItem(
        route = Routes.BOOKINGS,
        labelResId = R.string.nav_bookings_label,
        iconRes = R.drawable.calendar_today_24
    ),
    BottomNavItem(
        route = Routes.PROFILE,
        labelResId = R.string.nav_profile_label,
        iconRes = R.drawable.person_24
    )
)

fun isBottomNavRoute(route: String?): Boolean {
    return bottomNavItems.any { it.route == route }
}

fun NavHostController.navigateTopLevel(route: String) {
    navigate(route) {
        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}


@Composable
fun BottomNavigationBar(
    navController: NavHostController
) {
    val navBackStackEntry = navController.currentBackStackEntryAsState().value
    val currentDestination = navBackStackEntry?.destination

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = 10.dp)
            .background(MaterialTheme.colorScheme.surface)
    ) {
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.14f)
        )

        NavigationBar(
            modifier = Modifier.height(108.dp),
            containerColor = MaterialTheme.colorScheme.surface,
            tonalElevation = 0.dp,
            windowInsets = NavigationBarDefaults.windowInsets
        ) {
            bottomNavItems.forEach { item ->
                val label = stringResource(item.labelResId)
                val selected = currentDestination
                    ?.hierarchy
                    ?.any { it.route == item.route } == true

                NavigationBarItem(
                    selected = selected,
                    onClick = {
                        navController.navigateTopLevel(item.route)
                    },
                    icon = {
                        Icon(
                            painter = painterResource(id = item.iconRes),
                            contentDescription = label,
                            modifier = Modifier.size(30.dp)
                        )
                    },
                    label = {
                        Text(
                            text = label,
                            fontSize = 10.sp
                        )
                    },
                    alwaysShowLabel = true,
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = PrimaryBlue,
                        selectedTextColor = PrimaryBlue,
                        unselectedIconColor = MaterialTheme.colorScheme.onSurface,
                        unselectedTextColor = MaterialTheme.colorScheme.onSurface,
                        indicatorColor = Color.Transparent
                    )
                )
            }
        }
    }
}
