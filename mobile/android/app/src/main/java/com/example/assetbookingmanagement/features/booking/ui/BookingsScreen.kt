package com.example.assetbookingmanagement.features.booking.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.assetbookingmanagement.core.ui.components.AppEmptyState
import com.example.assetbookingmanagement.core.ui.components.AppLoadingState
import com.example.assetbookingmanagement.core.ui.components.AppMessageState

@Composable
fun BookingsScreen(
    onBookingClick: (MyBookingUiModel) -> Unit = {},
    viewModel: BookingsViewModel = hiltViewModel()
) {
    var selectedTabIndex by rememberSaveable { mutableIntStateOf(BookingsTab.MyBookings.ordinal) }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    BookingTabsLayout(
        selectedTabIndex = selectedTabIndex,
        tabLabels = BookingsTab.entries.map { it.label },
        onTabSelected = { selectedTabIndex = it }
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        when (BookingsTab.entries[selectedTabIndex]) {
            BookingsTab.MyBookings -> {
                BookingListContent(
                    isLoading = uiState.isLoading,
                    errorMessage = uiState.errorMessage,
                    bookings = uiState.myBookings,
                    emptyMessage = "No bookings.",
                    onBookingClick = onBookingClick
                )
            }

            BookingsTab.History -> {
                BookingListContent(
                    isLoading = uiState.isLoading,
                    errorMessage = uiState.errorMessage,
                    bookings = uiState.historyBookings,
                    emptyMessage = "No bookings.",
                    onBookingClick = onBookingClick
                )
            }
        }
    }
}

@Composable
private fun BookingListContent(
    isLoading: Boolean,
    errorMessage: String?,
    bookings: List<MyBookingUiModel>,
    emptyMessage: String,
    onBookingClick: (MyBookingUiModel) -> Unit
) {
    when {
        isLoading -> {
            AppLoadingState()
        }

        errorMessage != null -> {
            AppMessageState(
                title = "Couldn't load bookings",
                message = errorMessage
            )
        }

        bookings.isEmpty() -> {
            AppEmptyState(text = emptyMessage)
        }

        else -> {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(
                    items = bookings,
                    key = { booking -> booking.id }
                ) { booking ->
                    BookingCard(
                        booking = booking,
                        onClick = { onBookingClick(booking) }
                    )
                }
            }
        }
    }
}

private enum class BookingsTab(val label: String) {
    MyBookings("My bookings"),
    History("History")
}
