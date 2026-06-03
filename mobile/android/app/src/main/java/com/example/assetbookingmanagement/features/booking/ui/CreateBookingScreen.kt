package com.example.assetbookingmanagement.features.booking.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.assetbookingmanagement.R
import com.example.assetbookingmanagement.core.ui.components.AppButton
import com.example.assetbookingmanagement.core.ui.components.AvailabilityCalendar
import com.example.assetbookingmanagement.core.ui.components.DateTimePicker

@Composable
fun CreateBookingScreen(
    assetId: Long,
    onCancelClick: () -> Unit = {},
    onBookNowClick: () -> Unit = {},
    viewModel: CreateBookingViewModel = hiltViewModel()
) {
    var selectedTabIndex by rememberSaveable { mutableIntStateOf(BookingTab.ChooseDate.ordinal) }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(assetId) {
        viewModel.loadBookingPeriod(assetId)
    }

    LaunchedEffect(uiState.bookingCreated) {
        if (uiState.bookingCreated) {
            onBookNowClick()
        }
    }

    BookingTabsLayout(
        selectedTabIndex = selectedTabIndex,
        tabLabels = BookingTab.entries.map { it.label },
        onTabSelected = { selectedTabIndex = it }
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        when (selectedTabIndex) {
            BookingTab.ChooseDate.ordinal -> DateTimePicker(
                dateMillis = uiState.selectedDateMillis,
                startHour = uiState.startHour,
                startMinute = uiState.startMinute,
                endHour = uiState.endHour,
                endMinute = uiState.endMinute,
                hasSelectedStartTime = uiState.hasSelectedStartTime,
                hasSelectedEndTime = uiState.hasSelectedEndTime,
                unavailableHours = uiState.bookedHoursByDate[uiState.selectedDateMillis].orEmpty(),
                onDateSelected = viewModel::onDateSelected,
                onStartTimeSelected = viewModel::onStartTimeSelected,
                onEndTimeSelected = viewModel::onEndTimeSelected,
                showTimeInputs = uiState.bookingPeriod == "HOUR"
            )
            BookingTab.ShowAvailability.ordinal -> AvailabilityCalendar(
                availabilityByDate = uiState.availabilityByDate,
                onDateClick = { dateMillis ->
                    viewModel.onDateSelected(dateMillis)
                    selectedTabIndex = BookingTab.ChooseDate.ordinal
                }
            )
        }

        uiState.errorMessage?.let { message ->
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = message,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        BookingButtons(
            onCancelClick = onCancelClick,
            onBookNowClick = { viewModel.createBooking(assetId) },
            isSubmitting = uiState.isSubmitting,
            approvalRequired = uiState.approvalRequired == true
        )
    }
}

@Composable
private fun BookingButtons(
    onCancelClick: () -> Unit,
    onBookNowClick: () -> Unit,
    isSubmitting: Boolean,
    approvalRequired: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Button(
            onClick = onCancelClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.28f),
                contentColor = MaterialTheme.colorScheme.onSurface
            )
        ) {
            Text("Cancel", fontWeight = FontWeight.Bold)
        }

        AppButton(
            text = when {
                isSubmitting -> "Booking..."
                approvalRequired -> "Request booking"
                else -> "Book now"
            },
            iconRes = R.drawable.calendar_today_24,
            enabled = !isSubmitting,
            onClick = onBookNowClick
        )
    }
}

private enum class BookingTab(val label: String) {
    ChooseDate("Choose date"),
    ShowAvailability("Show Availability")
}
