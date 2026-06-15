package com.example.assetbookingmanagement.core.ui.components

import androidx.compose.ui.platform.LocalContext
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
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.example.assetbookingmanagement.core.ui.format.formatLocalizedDate
import com.example.assetbookingmanagement.core.ui.format.formatLocalizedTime
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateTimePicker(
    dateMillis: Long?,
    startHour: Int,
    startMinute: Int,
    endHour: Int,
    endMinute: Int,
    hasSelectedStartTime: Boolean = true,
    hasSelectedEndTime: Boolean = true,
    unavailableHours: Set<Int> = emptySet(),
    onDateSelected: (Long?) -> Unit,
    onStartTimeSelected: (Int, Int) -> Unit,
    onEndTimeSelected: (Int, Int) -> Unit,
    showTimeInputs: Boolean = true,
    modifier: Modifier = Modifier
) {
    var showStartDateDialog by rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current

    val startHourOptions = getAvailableHourOptions(
        selectedDateMillis = dateMillis,
        minHour = null
    )
    val endHourOptions = getAvailableHourOptions(
        selectedDateMillis = dateMillis,
        minHour = if (hasSelectedStartTime) startHour else null
    )

    Column(modifier = modifier.fillMaxWidth()) {
        DateField(
            dateValue = dateMillis?.let { formatLocalizedDate(it) }.orEmpty(),
            dateLabel = "Date",
            dateContentDescription = "Select date",
            onDateClick = { showStartDateDialog = true }
        )

        if (showTimeInputs) {
            Spacer(modifier = Modifier.height(16.dp))
            TimeRangeFieldRow(
                startTimeValue = if (hasSelectedStartTime) {
                    formatLocalizedTime(context, startHour, startMinute)
                } else {
                    ""
                },
                startTimeLabel = "From time",
                startTimeContentDescription = "Select from time",
                startOptions = startHourOptions,
                startDisabledOptions = unavailableHours,
                onStartTimeSelected = { hour -> onStartTimeSelected(hour, 0) },
                endTimeValue = if (hasSelectedEndTime) {
                    formatLocalizedTime(context, endHour, endMinute)
                } else {
                    ""
                },
                endTimeLabel = "To time",
                endTimeContentDescription = "Select to time",
                endOptions = endHourOptions,
                endDisabledOptions = getUnavailableEndHours(
                    endOptions = endHourOptions,
                    unavailableHours = unavailableHours,
                    hasSelectedStartTime = hasSelectedStartTime,
                    selectedStartHour = startHour
                ),
                onEndTimeSelected = { hour -> onEndTimeSelected(hour, 0) }
            )
        }
    }

    if (showStartDateDialog) {
        AppDatePickerDialog(
            initialSelectedDateMillis = dateMillis,
            onDismiss = { showStartDateDialog = false },
            onConfirm = {
                onDateSelected(it)
                showStartDateDialog = false
            }
        )
    }
}

@Composable
private fun TimeRangeFieldRow(
    startTimeValue: String,
    startTimeLabel: String,
    startTimeContentDescription: String,
    startOptions: List<Int>,
    startDisabledOptions: Set<Int>,
    onStartTimeSelected: (Int) -> Unit,
    endTimeValue: String,
    endTimeLabel: String,
    endTimeContentDescription: String,
    endOptions: List<Int>,
    endDisabledOptions: Set<Int>,
    onEndTimeSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        TimeDropdownField(
            value = startTimeValue,
            label = startTimeLabel,
            contentDescription = startTimeContentDescription,
            options = startOptions,
            disabledOptions = startDisabledOptions,
            onHourSelected = onStartTimeSelected,
            modifier = Modifier.weight(1f)
        )

        TimeDropdownField(
            value = endTimeValue,
            label = endTimeLabel,
            contentDescription = endTimeContentDescription,
            options = endOptions,
            disabledOptions = endDisabledOptions,
            onHourSelected = onEndTimeSelected,
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
    val currentHour = java.time.LocalTime.now().hour

    return (6..22).filter { hour ->
        when {
            selectedDate == today && hour < currentHour -> false
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
