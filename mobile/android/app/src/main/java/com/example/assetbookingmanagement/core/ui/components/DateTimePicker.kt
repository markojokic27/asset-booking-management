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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateTimePicker(
    startDateMillis: Long?,
    endDateMillis: Long?,
    startHour: Int,
    startMinute: Int,
    endHour: Int,
    endMinute: Int,
    onStartDateSelected: (Long?) -> Unit,
    onEndDateSelected: (Long?) -> Unit,
    onStartTimeSelected: (Int, Int) -> Unit,
    onEndTimeSelected: (Int, Int) -> Unit,
    showTimeInputs: Boolean = true,
    modifier: Modifier = Modifier
) {
    var showStartDateDialog by rememberSaveable { mutableStateOf(false) }
    var showEndDateDialog by rememberSaveable { mutableStateOf(false) }
    var showStartTimeDialog by rememberSaveable { mutableStateOf(false) }
    var showEndTimeDialog by rememberSaveable { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxWidth()) {
        DateTimeFieldRow(
            dateValue = startDateMillis?.let(::formatDate).orEmpty(),
            dateLabel = "Start date",
            dateContentDescription = "Select start date",
            onDateClick = { showStartDateDialog = true },
            timeValue = formatTime(startHour, startMinute),
            timeLabel = "Start time",
            timeContentDescription = "Select start time",
            onTimeClick = { showStartTimeDialog = true },
            showTimeInput = showTimeInputs
        )

        Spacer(modifier = Modifier.height(16.dp))
        DateTimeFieldRow(
            dateValue = endDateMillis?.let(::formatDate).orEmpty(),
            dateLabel = "End date",
            dateContentDescription = "Select end date",
            onDateClick = { showEndDateDialog = true },
            timeValue = formatTime(endHour, endMinute),
            timeLabel = "End time",
            timeContentDescription = "Select end time",
            onTimeClick = { showEndTimeDialog = true },
            showTimeInput = showTimeInputs
        )
    }

    if (showStartDateDialog) {
        AppDatePickerDialog(
            initialSelectedDateMillis = startDateMillis,
            onDismiss = { showStartDateDialog = false },
            onConfirm = {
                onStartDateSelected(it)
                showStartDateDialog = false
            }
        )
    }

    if (showEndDateDialog) {
        AppDatePickerDialog(
            initialSelectedDateMillis = endDateMillis,
            onDismiss = { showEndDateDialog = false },
            onConfirm = {
                onEndDateSelected(it)
                showEndDateDialog = false
            }
        )
    }

    if (showTimeInputs && showStartTimeDialog) {
        AppTimeInputDialog(
            initialHour = startHour,
            initialMinute = startMinute,
            onDismiss = { showStartTimeDialog = false },
            onConfirm = { hour, minute ->
                onStartTimeSelected(hour, minute)
                showStartTimeDialog = false
            }
        )
    }

    if (showTimeInputs && showEndTimeDialog) {
        AppTimeInputDialog(
            initialHour = endHour,
            initialMinute = endMinute,
            onDismiss = { showEndTimeDialog = false },
            onConfirm = { hour, minute ->
                onEndTimeSelected(hour, minute)
                showEndTimeDialog = false
            }
        )
    }
}

@Composable
private fun DateTimeFieldRow(
    dateValue: String,
    dateLabel: String,
    dateContentDescription: String,
    onDateClick: () -> Unit,
    timeValue: String,
    timeLabel: String,
    timeContentDescription: String,
    onTimeClick: () -> Unit,
    showTimeInput: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        DateTimeOutlinedField(
            value = dateValue,
            label = dateLabel,
            imageVector = Icons.Default.DateRange,
            contentDescription = dateContentDescription,
            onClick = onDateClick,
            modifier = Modifier.weight(1f)
        )

        if (showTimeInput) {
            DateTimeOutlinedField(
                value = timeValue,
                label = timeLabel,
                imageVector = Icons.Default.AccessTime,
                contentDescription = timeContentDescription,
                onClick = onTimeClick,
                modifier = Modifier.weight(1f)
            )
        }
    }
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
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppTimeInputDialog(
    initialHour: Int,
    initialMinute: Int,
    onDismiss: () -> Unit,
    onConfirm: (Int, Int) -> Unit
) {
    val timePickerState = rememberTimePickerState(
        initialHour = initialHour,
        initialMinute = initialMinute,
        is24Hour = true
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surface,
        title = {
            Text(
                text = "Select time",
                style = MaterialTheme.typography.titleMedium
            )
        },
        text = {
            TimeInput(
                state = timePickerState,
                colors = TimePickerDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    periodSelectorSelectedContainerColor = MaterialTheme.colorScheme.primary,
                    periodSelectorSelectedContentColor = MaterialTheme.colorScheme.onPrimary,
                    timeSelectorSelectedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.14f),
                    timeSelectorSelectedContentColor = MaterialTheme.colorScheme.primary,
                    selectorColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirm(timePickerState.hour, timePickerState.minute) }
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

private fun formatDate(millis: Long): String {
    val formatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
}

private fun formatTime(hour: Int, minute: Int): String {
    return "%02d:%02d".format(hour, minute)
}
