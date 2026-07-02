package com.example.assetbookingmanagement.features.booking.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.assetbookingmanagement.R
import com.example.assetbookingmanagement.core.ui.components.AppEmptyState
import com.example.assetbookingmanagement.core.ui.components.AppLoadingState
import com.example.assetbookingmanagement.core.ui.components.AppMessageState
import com.example.assetbookingmanagement.core.ui.components.SearchBar

@Composable
fun BookingsScreen(
    onBookingClick: (MyBookingUiModel) -> Unit = {},
    viewModel: BookingsViewModel = hiltViewModel()
) {
    var selectedTabIndex by rememberSaveable { mutableIntStateOf(BookingsTab.MyBookings.ordinal) }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val selectedTab = BookingsTab.entries[selectedTabIndex]

    LifecycleResumeEffect(Unit) {
        viewModel.refreshBookingsData()
        onPauseOrDispose { }
    }

    val tabLabels = BookingsTab.entries.map { tab ->
        when (tab) {
            BookingsTab.MyBookings -> stringResource(R.string.home_my_bookings_label)
            BookingsTab.History -> stringResource(R.string.bookings_tab_history)
        }
    }
    val hasBookingsForSelectedTab = when (selectedTab) {
        BookingsTab.MyBookings -> uiState.myBookings.isNotEmpty()
        BookingsTab.History -> uiState.historyBookings.isNotEmpty()
    }

    BookingTabsLayout(
        selectedTabIndex = selectedTabIndex,
        tabLabels = tabLabels,
        onTabSelected = { selectedTabIndex = it }
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        if (hasBookingsForSelectedTab) {
            SearchBar(
                value = uiState.searchText,
                onValueChange = viewModel::onSearchTextChange,
                placeholder = stringResource(R.string.common_search_bookings_placeholder)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        when (selectedTab) {
            BookingsTab.MyBookings -> {
                BookingListContent(
                    isLoading = uiState.isLoading,
                    errorMessageResId = uiState.errorMessageResId,
                    bookings = uiState.filteredMyBookings,
                    emptyMessage = if (uiState.myBookings.isEmpty()) {
                        stringResource(R.string.bookings_empty_no_bookings)
                    } else {
                        stringResource(R.string.bookings_empty_no_matching)
                    },
                    onBookingClick = onBookingClick
                )
            }

            BookingsTab.History -> {
                BookingListContent(
                    isLoading = uiState.isLoading,
                    errorMessageResId = uiState.errorMessageResId,
                    bookings = uiState.filteredHistoryBookings,
                    emptyMessage = if (uiState.historyBookings.isEmpty()) {
                        stringResource(R.string.bookings_empty_no_bookings)
                    } else {
                        stringResource(R.string.bookings_empty_no_matching)
                    },
                    onBookingClick = onBookingClick
                )
            }
        }
    }
}

@Composable
private fun BookingListContent(
    isLoading: Boolean,
    errorMessageResId: Int?,
    bookings: List<MyBookingUiModel>,
    emptyMessage: String,
    onBookingClick: (MyBookingUiModel) -> Unit
) {
    when {
        isLoading -> {
            AppLoadingState()
        }

        errorMessageResId != null -> {
            AppMessageState(
                title = stringResource(R.string.bookings_error_load_title),
                message = stringResource(errorMessageResId)
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

private enum class BookingsTab {
    MyBookings,
    History
}
