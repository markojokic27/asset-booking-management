package com.example.assetbookingmanagement.features.booking.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun BookingsScreen(
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
                    emptyMessage = "No current or upcoming bookings found."
                )
            }

            BookingsTab.History -> {
                BookingListContent(
                    isLoading = uiState.isLoading,
                    errorMessage = uiState.errorMessage,
                    bookings = uiState.historyBookings,
                    emptyMessage = "No bookings found."
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
    emptyMessage: String
) {
    when {
        isLoading -> {
            Text(text = "Loading bookings...")
        }

        errorMessage != null -> {
            Text(text = errorMessage)
        }

        bookings.isEmpty() -> {
            Text(text = emptyMessage)
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
                    BookingCard(booking = booking)
                }
            }
        }
    }
}

private enum class BookingsTab(val label: String) {
    MyBookings("My bookings"),
    History("History")
}
