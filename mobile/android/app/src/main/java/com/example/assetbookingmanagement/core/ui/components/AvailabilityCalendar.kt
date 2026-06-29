package com.example.assetbookingmanagement.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.assetbookingmanagement.R
import com.example.assetbookingmanagement.features.booking.ui.AvailabilityStatus
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneOffset
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun AvailabilityCalendar(
    availabilityByDate: Map<Long, AvailabilityStatus>,
    onDateClick: (Long) -> Unit,
    onMonthChange: (YearMonth) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }
    var selectedDateMillis by remember { mutableLongStateOf(0L) }
    val locale = Locale.getDefault()
    val daysOfWeek = remember(locale) {
        DayOfWeek.entries.map { dayOfWeek ->
            dayOfWeek.getDisplayName(TextStyle.SHORT, locale)
        }
    }
    val calendarWeeks = remember(currentMonth) {
        createMonthGrid(currentMonth).chunked(7)
    }

    LaunchedEffect(currentMonth) {
        onMonthChange(currentMonth)
    }

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { currentMonth = currentMonth.minusMonths(1) }) {
                    Icon(
                        Icons.Default.ChevronLeft,
                        contentDescription = stringResource(R.string.create_booking_previous_month)
                    )
                }

                Text(
                    text = formatMonthYear(currentMonth, locale),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                IconButton(onClick = { currentMonth = currentMonth.plusMonths(1) }) {
                    Icon(
                        Icons.Default.ChevronRight,
                        contentDescription = stringResource(R.string.create_booking_next_month)
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp)
            ) {
                daysOfWeek.forEach { dayLabel ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(3.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = dayLabel,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            calendarWeeks.forEach { week ->
                Row(modifier = Modifier.fillMaxWidth()) {
                    week.forEach { date ->
                        val dateMillis = date?.toUtcMillis()
                        AvailabilityCalendarDay(
                            date = date,
                            dayStatus = dateMillis?.let(availabilityByDate::get),
                            isSelected = dateMillis == selectedDateMillis,
                            onClick = {
                                if (dateMillis != null) {
                                    selectedDateMillis = dateMillis
                                    onDateClick(dateMillis)
                                }
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AvailabilityCalendarDay(
    date: LocalDate?,
    dayStatus: AvailabilityStatus?,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (date == null) {
        Box(
            modifier = modifier
                .aspectRatio(1f)
                .padding(3.dp)
        )
        return
    }


    val (backgroundColor, textColor) = when (dayStatus) {
        AvailabilityStatus.DAY_BOOKED -> MaterialTheme.colorScheme.error.copy(alpha = 0.16f) to
            MaterialTheme.colorScheme.error
        AvailabilityStatus.HOUR_BOOKED -> Color(0xFFFFC107).copy(alpha = 0.24f) to Color(0xFF9A6700)
        null -> Color.Transparent to MaterialTheme.colorScheme.onSurface
    }

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .padding(3.dp)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .background(backgroundColor, CircleShape)
                .border(
                    width = if (isSelected) 1.5.dp else 0.dp,
                    color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = date.dayOfMonth.toString(),
                style = MaterialTheme.typography.bodySmall,
                color = textColor,
                fontWeight = if (dayStatus != null) FontWeight.SemiBold else FontWeight.Normal,
                textAlign = TextAlign.Center
            )
        }
    }
}

// Generates a list of the days to display in the calendar grid for a given month
private fun createMonthGrid(month: YearMonth): List<LocalDate?> {
    val firstDayOfMonth = month.atDay(1)
    val offset = firstDayOfMonth.dayOfWeek.value - 1
    return buildList {
        repeat(offset) { add(null) }
        (1..month.lengthOfMonth()).forEach { day ->
            add(month.atDay(day))
        }
        while (size % 7 != 0) {
            add(null)
        }
    }
}

private fun formatMonthYear(month: YearMonth, locale: Locale): String {
    val monthName = month.month.getDisplayName(TextStyle.FULL_STANDALONE, locale)
    val formattedMonthName = monthName.replaceFirstChar {
        if (it.isLowerCase()) it.titlecase(locale) else it.toString()
    }
    return "$formattedMonthName ${month.year}"
}

private fun LocalDate.toUtcMillis(): Long =
    atStartOfDay()
        .toInstant(ZoneOffset.UTC)
        .toEpochMilli()
