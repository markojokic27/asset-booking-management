//
//  BookingPeriodSection.swift
//  Asset Booking Management
//
//  Created by Jakov  Petric on 13.07.2026..
//

import Foundation
import SwiftUI

struct BookingPeriodSection: View {
    let mode: BookingMode

    @Binding var fromDate: Date
    @Binding var toDate: Date

    @Binding var bookingDate: Date

    @Binding var startHour: Int
    @Binding var endHour: Int
    
    @Binding var selectedMonth: Date
    @Binding var selectedWeekdays: Set<Int>
    
    @Binding var recurringMode: RecurringMode
    
    let unavailableHours: Set<Int>

    private let allowedHours = Array(6...22)

    var body: some View {
        Section("Booking Period") {
            switch mode {
            case .daily:
                dailyView
            case .hourly:
                hourlyView
            case .recurring:
                recurringView
            }
        }
    }
}


// MARK: - Daily
private extension BookingPeriodSection {
    @ViewBuilder
    var dailyView: some View {
        
        DatePicker(
            "From",
            selection: $fromDate,
            displayedComponents: .date
        )
        .onChange(of: fromDate) { _, newValue in

            if toDate < newValue {
                toDate = newValue
            }
        }

        DatePicker(
            "To",
            selection: $toDate,
            in: fromDate...,
            displayedComponents: .date
        )
    }
}


// MARK: - Hourly
private extension BookingPeriodSection {
    @ViewBuilder
    var hourlyView: some View {
        DatePicker(
            "Date",
            selection: $bookingDate,
            displayedComponents: .date
        )

        Picker("From", selection: $startHour) {
            ForEach(startHourOptions, id: \.self) { hour in
                Text(String(format: "%02d:00", hour))
                    .tag(hour)
            }
        }
        .onChange(of: startHour) { _, newStartHour in
            if !endHourOptions.contains(endHour) {
                endHour = endHourOptions.first ?? newStartHour + 1
            }
        }

        Picker("To", selection: $endHour) {
            ForEach(endHourOptions, id: \.self) { hour in
                Text(String(format: "%02d:00", hour))
                    .tag(hour)
            }
        }
    }

    private var startHourOptions: [Int] {
        allowedHours.filter { start in
            !unavailableHours.contains(start) &&
            allowedHours.contains { end in
                isEndHourAvailable(end, for: start)
            }
        }
    }

    private var endHourOptions: [Int] {
        allowedHours.filter { hour in
            isEndHourAvailable(
                hour,
                for: startHour
            )
        }
    }

    private func isEndHourAvailable(
        _ end: Int,
        for start: Int
    ) -> Bool {
        guard end > start else {
            return false
        }

        let requestedHours = start..<end

        return requestedHours.allSatisfy {
            !unavailableHours.contains($0)
        }
    }
}

// MARK: - Recurring
private extension BookingPeriodSection {
    
    @ViewBuilder
    private var recurringView: some View {
        Section {
            Picker("", selection: $recurringMode) {
                ForEach(RecurringMode.allCases, id: \.self) { mode in
                    Text(mode.rawValue).tag(mode)
                }
            }
            .pickerStyle(.segmented)
        }

        switch recurringMode {
        case .range:
            dailyView

        case .monthly:
            monthlyView
        }
    }
    
    @ViewBuilder
    private var monthlyView: some View {
        DatePicker(
            "Month",
            selection: $selectedMonth,
            displayedComponents: .date
        )
        .datePickerStyle(.compact)
        .onChange(of: selectedMonth) { _, newValue in
            print("Selected month changed:", newValue)
        }

        WeekdaySelector(
            selectedDays: $selectedWeekdays
        )
    }
}

// MARK: - Weekday Selector
struct WeekdaySelector: View {

    @Binding var selectedDays: Set<Int>

    private let weekdays = [
        ("Mon", 2),
        ("Tue", 3),
        ("Wed", 4),
        ("Thu", 5),
        ("Fri", 6),
        ("Sat", 7)
    ]
    var body: some View {
        VStack(alignment: .leading, spacing: 12) {
            Text("Recurring days")
            HStack(spacing: 8) {
                ForEach(weekdays, id: \.1) { weekday in
                    let day = weekday.1
                    Button {
                        if selectedDays.contains(day) {
                            selectedDays.remove(day)
                        } else {
                            selectedDays.insert(day)
                        }
                    } label: {
                        Text(weekday.0)
                            .frame(maxWidth: .infinity)
                            .padding(.vertical, 8)
                            .background(
                                selectedDays.contains(day)
                                    ? Color.accentColor
                                    : Color.gray.opacity(0.2)
                            )
                            .foregroundStyle(
                                selectedDays.contains(day)
                                    ? .white
                                    : .primary
                            )
                            .clipShape(Capsule())
                    }
                    .buttonStyle(.plain)
                }
            }
        }
    }
}
