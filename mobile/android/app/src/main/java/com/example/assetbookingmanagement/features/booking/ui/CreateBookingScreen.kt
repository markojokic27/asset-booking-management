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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.assetbookingmanagement.R
import com.example.assetbookingmanagement.core.ui.components.AppButton
import com.example.assetbookingmanagement.core.ui.components.AvailabilityCalendar
import com.example.assetbookingmanagement.core.ui.components.DateRangeSelection
import com.example.assetbookingmanagement.core.ui.components.DateTimePicker
import com.example.assetbookingmanagement.core.ui.components.DateTimePickerCallbacks
import com.example.assetbookingmanagement.core.ui.components.DateTimePickerState
import com.example.assetbookingmanagement.core.ui.components.TimeSelection
import com.example.assetbookingmanagement.core.ui.format.formatLocalizedBookingDisplayText

@Composable
fun CreateBookingScreen(
    assetId: Long,
    onCancelClick: () -> Unit = {},
    onBookNowClick: (assetName: String, fromDate: String, toDate: String, approvalRequired: Boolean) -> Unit = { _, _, _, _ -> },
    viewModel: CreateBookingViewModel = hiltViewModel()
) {
    var selectedTabIndex by rememberSaveable { mutableIntStateOf(BookingTab.ChooseDate.ordinal) }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LifecycleResumeEffect(assetId) {
        viewModel.loadBookingPeriod(assetId)

        onPauseOrDispose { }
    }

    LaunchedEffect(uiState.bookingCreated) {
        val createdBookingStart = uiState.createdBookingStart
        val createdBookingEnd = uiState.createdBookingEnd

        if (uiState.bookingCreated && createdBookingStart != null && createdBookingEnd != null) {
            onBookNowClick(
                uiState.assetName,
                formatLocalizedBookingDisplayText(
                    dateTimeText = createdBookingStart,
                    context = context,
                    isHourlyBooking = uiState.bookingPeriod == "HOUR"
                ),
                formatLocalizedBookingDisplayText(
                    dateTimeText = createdBookingEnd,
                    context = context,
                    isHourlyBooking = uiState.bookingPeriod == "HOUR"
                ),
                uiState.approvalRequired == true
            )
        }
    }

    BookingTabsLayout(
        selectedTabIndex = selectedTabIndex,
        tabLabels = BookingTab.entries.map { stringResource(it.labelRes) },
        onTabSelected = { selectedTabIndex = it }
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        when (selectedTabIndex) {
            BookingTab.ChooseDate.ordinal -> DateTimePicker(
                state = DateTimePickerState(
                    dateRange = DateRangeSelection(
                        fromDateMillis = uiState.selectedFromDateMillis,
                        toDateMillis = uiState.selectedToDateMillis
                    ),
                    startTime = TimeSelection(
                        hour = uiState.startHour,
                        minute = uiState.startMinute,
                        hasSelected = uiState.hasSelectedStartTime
                    ),
                    endTime = TimeSelection(
                        hour = uiState.endHour,
                        minute = uiState.endMinute,
                        hasSelected = uiState.hasSelectedEndTime
                    ),
                    unavailableHours = uiState.bookedHoursByDate[uiState.selectedFromDateMillis].orEmpty(),
                    showTimeInputs = uiState.bookingPeriod == "HOUR"
                ),
                callbacks = DateTimePickerCallbacks(
                    onFromDateSelected = viewModel::onFromDateSelected,
                    onToDateSelected = viewModel::onToDateSelected,
                    onStartTimeSelected = viewModel::onStartTimeSelected,
                    onEndTimeSelected = viewModel::onEndTimeSelected
                )
            )
            BookingTab.ShowAvailability.ordinal -> AvailabilityCalendar(
                availabilityByDate = uiState.availabilityByDate,
                onMonthChange = viewModel::onVisibleMonthChanged,
                onDateClick = { dateMillis ->
                    viewModel.onFromDateSelected(dateMillis)
                    selectedTabIndex = BookingTab.ChooseDate.ordinal
                }
            )
        }

        if (
            selectedTabIndex == BookingTab.ChooseDate.ordinal &&
            uiState.categoryName.equals("Parking", ignoreCase = true)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            RecurringDaysSelector(
                selectedDays = uiState.selectedWeekdays,
                onDayToggle = viewModel::onRecurringWeekdayToggled
            )
        }

        uiState.errorMessageRes?.let { messageRes ->
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(messageRes),
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
            Text(
                text = stringResource(R.string.common_cancel),
                fontWeight = FontWeight.Bold
            )
        }

        AppButton(
            text = when {
                isSubmitting -> stringResource(R.string.create_booking_submit_loading)
                approvalRequired -> stringResource(R.string.create_booking_submit_request)
                else -> stringResource(R.string.create_booking_submit_now)
            },
            iconRes = R.drawable.calendar_today_24,
            enabled = !isSubmitting,
            onClick = onBookNowClick
        )
    }
}

private enum class BookingTab(val labelRes: Int) {
    ChooseDate(R.string.create_booking_tab_choose_date),
    ShowAvailability(R.string.create_booking_tab_show_availability)
}
