package com.example.assetbookingmanagement.core.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AvailabilityCalendar(
    modifier: Modifier = Modifier
) {
    val datePickerState = rememberDatePickerState()

    DatePicker(
        state = datePickerState,
        modifier = modifier.fillMaxWidth(),
        title = null,
        headline = null,
        showModeToggle = false,
        colors = DatePickerDefaults.colors(
            containerColor = MaterialTheme.colorScheme.background
        )
    )
}


