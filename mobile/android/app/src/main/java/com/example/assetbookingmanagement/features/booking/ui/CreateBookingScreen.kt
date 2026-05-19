package com.example.assetbookingmanagement.features.booking.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.assetbookingmanagement.R
import com.example.assetbookingmanagement.core.ui.components.AppButton
import com.example.assetbookingmanagement.core.ui.components.DateTimePicker

@Composable
fun CreateBookingScreen(
    assetId: Long,
    onCancelClick: () -> Unit = {},
    onBookNowClick: () -> Unit = {}
) {
    var selectedTabIndex by rememberSaveable { mutableIntStateOf(BookingTab.ChooseDate.ordinal) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.Top
    ) {
        TabRow(
            selectedTabIndex = selectedTabIndex,
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth(),
            containerColor = MaterialTheme.colorScheme.background,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                    height = 2.dp,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        ) {
            BookingTab.entries.forEach { tab ->
                Tab(
                    selected = selectedTabIndex == tab.ordinal,
                    onClick = { selectedTabIndex = tab.ordinal },
                    text = {
                        Text(
                            text = tab.label,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        if (selectedTabIndex == BookingTab.ChooseDate.ordinal) {
            DateTimePicker()
        }

        Spacer(modifier = Modifier.weight(1f))

        BookingButtons(
            onCancelClick = onCancelClick,
            onBookNowClick = onBookNowClick
        )
    }
}

@Composable
private fun BookingButtons(
    onCancelClick: () -> Unit,
    onBookNowClick: () -> Unit
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
            text = "Book now",
            iconRes = R.drawable.calendar_today_24,
            onClick = onBookNowClick
        )
    }
}

private enum class BookingTab(val label: String) {
    ChooseDate("Choose date"),
    ShowAvailability("Show Availability")
}
