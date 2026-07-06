package com.example.assetbookingmanagement.core.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.example.assetbookingmanagement.R
import com.example.assetbookingmanagement.core.ui.format.formatLocalizedDate
import com.example.assetbookingmanagement.core.ui.format.formatLocalizedTime
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneOffset

data class DateRangeSelection(
    val fromDateMillis: Long? = null,
    val toDateMillis: Long? = null
)

data class TimeSelection(
    val hour: Int,
    val minute: Int,
    val hasSelected: Boolean = true
)

data class DateTimePickerState(
    val dateRange: DateRangeSelection,
    val startTime: TimeSelection,
    val endTime: TimeSelection,
    val unavailableHours: Set<Int> = emptySet(),
    val showTimeInputs: Boolean = true
)

data class DateTimePickerCallbacks(
    val onFromDateSelected: (Long?) -> Unit,
    val onToDateSelected: (Long?) -> Unit,
    val onStartTimeSelected: (Int, Int) -> Unit,
    val onEndTimeSelected: (Int, Int) -> Unit
)

private data class TimeFieldConfig(
    val value: String,
    val label: String,
    val contentDescription: String,
    val options: List<Int>,
    val disabledOptions: Set<Int>,
    val onHourSelected: (Int) -> Unit
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateTimePicker(
    state: DateTimePickerState,
    callbacks: DateTimePickerCallbacks,
    modifier: Modifier = Modifier
) {
    var showFromDateDialog by rememberSaveable { mutableStateOf(false) }
    var showToDateDialog by rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current
    val fromDateValue = state.dateRange.fromDateMillis?.let(::formatLocalizedDate).orEmpty()
    val toDateValue = state.dateRange.toDateMillis?.let(::formatLocalizedDate).orEmpty()
    val startTimeValue = selectedTimeValue(
        hasSelectedTime = state.startTime.hasSelected,
        context = context,
        hour = state.startTime.hour,
        minute = state.startTime.minute
    )
    val endTimeValue = selectedTimeValue(
        hasSelectedTime = state.endTime.hasSelected,
        context = context,
        hour = state.endTime.hour,
        minute = state.endTime.minute
    )

    val startHourOptions = getAvailableHourOptions(
        selectedDateMillis = state.dateRange.fromDateMillis,
        minHour = null
    )
    val endHourOptions = getAvailableHourOptions(
        selectedDateMillis = state.dateRange.fromDateMillis,
        minHour = if (state.startTime.hasSelected) state.startTime.hour else null
    )

    Column(modifier = modifier.fillMaxWidth()) {
        DateSection(
            showTimeInputs = state.showTimeInputs,
            fromDateValue = fromDateValue,
            toDateValue = toDateValue,
            onFromDateClick = { showFromDateDialog = true },
            onToDateClick = { showToDateDialog = true }
        )

        if (state.showTimeInputs) {
            Spacer(modifier = Modifier.height(16.dp))
            TimeRangeFieldRow(
                startField = TimeFieldConfig(
                    value = startTimeValue,
                    label = stringResource(R.string.common_from),
                    contentDescription = stringResource(R.string.create_booking_select_from_time),
                    options = startHourOptions,
                    disabledOptions = state.unavailableHours,
                    onHourSelected = { hour -> callbacks.onStartTimeSelected(hour, 0) }
                ),
                endField = TimeFieldConfig(
                    value = endTimeValue,
                    label = stringResource(R.string.common_to),
                    contentDescription = stringResource(R.string.create_booking_select_to_time),
                    options = endHourOptions,
                    disabledOptions = getUnavailableEndHours(
                    endOptions = endHourOptions,
                    unavailableHours = state.unavailableHours,
                    hasSelectedStartTime = state.startTime.hasSelected,
                    selectedStartHour = state.startTime.hour
                ),
                    onHourSelected = { hour -> callbacks.onEndTimeSelected(hour, 0) }
                )
            )
        }
    }

    if (showFromDateDialog) {
        AppDatePickerDialog(
            initialSelectedDateMillis = state.dateRange.fromDateMillis,
            onDismiss = { showFromDateDialog = false },
            onConfirm = {
                callbacks.onFromDateSelected(it)
                showFromDateDialog = false
            }
        )
    }

    if (showToDateDialog) {
        AppDatePickerDialog(
            initialSelectedDateMillis = state.dateRange.toDateMillis,
            onDismiss = { showToDateDialog = false },
            onConfirm = {
                callbacks.onToDateSelected(it)
                showToDateDialog = false
            }
        )
    }
}

@Composable
private fun DateSection(
    showTimeInputs: Boolean,
    fromDateValue: String,
    toDateValue: String,
    onFromDateClick: () -> Unit,
    onToDateClick: () -> Unit
) {
    if (showTimeInputs) {
        DateField(
            dateValue = fromDateValue,
            dateLabel = stringResource(R.string.create_booking_date),
            dateContentDescription = stringResource(R.string.create_booking_select_date),
            onDateClick = onFromDateClick
        )
        return
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        DateField(
            dateValue = fromDateValue,
            dateLabel = stringResource(R.string.common_from),
            dateContentDescription = stringResource(R.string.create_booking_select_from_date),
            onDateClick = onFromDateClick,
            modifier = Modifier.weight(1f)
        )
        DateField(
            dateValue = toDateValue,
            dateLabel = stringResource(R.string.common_to),
            dateContentDescription = stringResource(R.string.create_booking_select_to_date),
            onDateClick = onToDateClick,
            modifier = Modifier.weight(1f)
        )
    }
}

private fun selectedTimeValue(
    hasSelectedTime: Boolean,
    context: android.content.Context,
    hour: Int,
    minute: Int
): String {
    if (!hasSelectedTime) {
        return ""
    }

    return formatLocalizedTime(context, hour, minute)
}

@Composable
private fun TimeRangeFieldRow(
    startField: TimeFieldConfig,
    endField: TimeFieldConfig,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        TimeDropdownField(
            value = startField.value,
            label = startField.label,
            contentDescription = startField.contentDescription,
            options = startField.options,
            disabledOptions = startField.disabledOptions,
            onHourSelected = startField.onHourSelected,
            modifier = Modifier.weight(1f)
        )

        TimeDropdownField(
            value = endField.value,
            label = endField.label,
            contentDescription = endField.contentDescription,
            options = endField.options,
            disabledOptions = endField.disabledOptions,
            onHourSelected = endField.onHourSelected,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun DateField(
    dateValue: String,
    dateLabel: String,
    dateContentDescription: String,
    onDateClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    DateTimeOutlinedField(
        value = dateValue,
        label = dateLabel,
        imageVector = Icons.Default.DateRange,
        contentDescription = dateContentDescription,
        onClick = onDateClick,
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
private fun DateTimeOutlinedField(
    value: String,
    label: String,
    imageVector: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = {},
        readOnly = true,
        singleLine = true,
        label = {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall
            )
        },
        placeholder = {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall
            )
        },
        trailingIcon = {
            IconButton(onClick = onClick) {
                Icon(
                    imageVector = imageVector,
                    contentDescription = contentDescription
                )
            }
        },
        textStyle = MaterialTheme.typography.bodySmall,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TimeDropdownField(
    value: String,
    label: String,
    contentDescription: String,
    options: List<Int>,
    disabledOptions: Set<Int>,
    onHourSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = {},
            readOnly = true,
            singleLine = true,
            label = {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall
                )
            },
            placeholder = {
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodySmall
                )
            },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.AccessTime,
                    contentDescription = contentDescription
                )
            },
            textStyle = MaterialTheme.typography.bodySmall,
            modifier = Modifier
                .menuAnchor(
                    type = MenuAnchorType.PrimaryNotEditable,
                    enabled = true
                )
                .fillMaxWidth()
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            offset = DpOffset(x = 0.dp, y = 4.dp),
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            options.forEach { hour ->
                val hourLabel = formatLocalizedTime(context, hour, 0)
                val isDisabled = hour in disabledOptions
                DropdownMenuItem(
                    text = {
                        Text(
                            text = hourLabel,
                            color = if (isDisabled) {
                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                            } else {
                                MaterialTheme.colorScheme.onSurface
                            }
                        )
                    },
                    enabled = !isDisabled,
                    onClick = {
                        onHourSelected(hour)
                        expanded = false
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppDatePickerDialog(
    initialSelectedDateMillis: Long?,
    onDismiss: () -> Unit,
    onConfirm: (Long?) -> Unit
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialSelectedDateMillis
    )
    // Reuse the same app-tinted palette for both the dialog chrome and the calendar content
    val datePickerColors = DatePickerDefaults.colors(
        containerColor = MaterialTheme.colorScheme.surface,
        navigationContentColor = MaterialTheme.colorScheme.primary,
        currentYearContentColor = MaterialTheme.colorScheme.primary,
        selectedYearContainerColor = MaterialTheme.colorScheme.primary,
        selectedYearContentColor = MaterialTheme.colorScheme.onPrimary,
        selectedDayContainerColor = MaterialTheme.colorScheme.primary,
        selectedDayContentColor = MaterialTheme.colorScheme.onPrimary,
        todayContentColor = MaterialTheme.colorScheme.primary,
        todayDateBorderColor = MaterialTheme.colorScheme.primary
    )

    DatePickerDialog(
        onDismissRequest = onDismiss,
        colors = datePickerColors,
        confirmButton = {
            TextButton(onClick = { onConfirm(datePickerState.selectedDateMillis) }) {
                Text(stringResource(R.string.common_ok))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.common_cancel))
            }
        }
    ) {
        DatePicker(
            state = datePickerState,
            showModeToggle = false,
            colors = datePickerColors
        )
    }
}

private fun getAvailableHourOptions(
    selectedDateMillis: Long?,
    minHour: Int?
): List<Int> {
    val selectedDate = selectedDateMillis?.let {
        Instant.ofEpochMilli(it)
            .atZone(ZoneOffset.UTC)
            .toLocalDate()
    }
    val today = LocalDate.now()
    val currentTime = LocalTime.now()
    val firstAvailableHourToday = if (currentTime.minute == 0 && currentTime.second == 0) {
        currentTime.hour
    } else {
        currentTime.hour + 1
    }

    return (6..22).filter { hour ->
        when {
            selectedDate == today && hour < firstAvailableHourToday -> false
            minHour != null -> hour > minHour
            else -> true
        }
    }
}

private fun getUnavailableEndHours(
    endOptions: List<Int>,
    unavailableHours: Set<Int>,
    hasSelectedStartTime: Boolean,
    selectedStartHour: Int
): Set<Int> {
    if (!hasSelectedStartTime) {
        return unavailableHours
    }

    return endOptions.filter { endHour ->
        unavailableHours.any { bookedHour -> bookedHour in selectedStartHour until endHour }
    }.toSet()
}
