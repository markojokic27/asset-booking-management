//
//  AvailabilityCalendar.swift
//  Asset Booking Management
//
//  Created by Jakov  Petric on 13.07.2026..
//

import SwiftUI

struct AvailabilityCalendarView: View {
    let bookings: [BookingResponse]
    
    @State private var displayedMonth = Date()

    private var calendar: Calendar {
        var calendar = Calendar.current
        calendar.firstWeekday = 2
        return calendar
    }
    private func hasBooking(_ date: Date) -> Bool {
        bookings.contains { booking in
            let day = calendar.startOfDay(for: date)
            let start = calendar.startOfDay(for: booking.bookingStart)
            let end = calendar.startOfDay(for: booking.bookingEnd)

            return day >= start && day <= end
        }
    }
    
    private func isPast(_ date: Date) -> Bool {
        calendar.startOfDay(for: date) <
        calendar.startOfDay(for: Date())
    }

    private func dayBackground(_ date: Date) -> Color {
        if isPast(date) {
            return Color.gray.opacity(0.05)
        }

        if hasBooking(date) {
            return Color.red.opacity(0.25)
        }

        return Color.gray.opacity(0.12)
    }

    private func dayForeground(_ date: Date) -> Color {
        if isPast(date) {
            return .secondary
        }

        return .primary
    }
    
    var body: some View {
        VStack(spacing: 20) {

            monthHeader

            weekdayHeader

            monthGrid
        }
        .padding()
    }

    private var monthHeader: some View {
        HStack {

            Button {
                displayedMonth = calendar.date(
                    byAdding: .month,
                    value: -1,
                    to: displayedMonth
                )!
            } label: {
                Image(systemName: "chevron.left")
            }

            Spacer()

            Text(displayedMonth.formatted(.dateTime.month(.wide).year()))
                .font(.headline)

            Spacer()

            Button {
                displayedMonth = calendar.date(
                    byAdding: .month,
                    value: 1,
                    to: displayedMonth
                )!
            } label: {
                Image(systemName: "chevron.right")
            }
        }
    }

    private var weekdayHeader: some View {
        let weekdays = reorderedWeekdays

        return LazyVGrid(columns: columns) {
            ForEach(weekdays, id: \.self) { weekday in
                Text(weekday)
                    .font(.caption)
                    .fontWeight(.semibold)
                    .foregroundStyle(.secondary)
            }
        }
    }

    private var reorderedWeekdays: [String] {
        let symbols = calendar.shortStandaloneWeekdaySymbols

        return Array(symbols.dropFirst()) + [symbols[0]]
    }

    private var monthGrid: some View {
        LazyVGrid(columns: columns, spacing: 12) {
            ForEach(days.indices, id: \.self) { index in
                if let day = days[index] {
                    Text("\(calendar.component(.day, from: day))")
                        .frame(maxWidth: .infinity)
                        .frame(height: 40)
                        .foregroundStyle(dayForeground(day))
                        .background(dayBackground(day))
                        .clipShape(RoundedRectangle(cornerRadius: 8))
                } else {
                    Color.clear
                        .frame(height: 40)
                }
            }
        }
    }

    private var columns: [GridItem] {
        Array(repeating: GridItem(.flexible()), count: 7)
    }

    private var days: [Date?] {
        guard
            let monthInterval = calendar.dateInterval(of: .month, for: displayedMonth),
            let firstWeek = calendar.dateInterval(of: .weekOfMonth, for: monthInterval.start),
            let lastWeek = calendar.dateInterval(
                of: .weekOfMonth,
                for: monthInterval.end.addingTimeInterval(-1)
            )
        else {
            return []
        }

        var days: [Date?] = []

        var date = firstWeek.start

        while date < lastWeek.end {
            if calendar.isDate(date, equalTo: displayedMonth, toGranularity: .month) {
                days.append(date)
            } else {
                days.append(nil)
            }

            date = calendar.date(byAdding: .day, value: 1, to: date)!
        }

        return days
    }
}
