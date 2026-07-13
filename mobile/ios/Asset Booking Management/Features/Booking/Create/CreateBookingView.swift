//
//  CreateBookingView.swift
//  Asset Booking Management
//
//  Created by Jakov  Petric on 10.07.2026..
//

import SwiftUI

enum RecurringMode: String, CaseIterable {
    case range = "Regular"
    case monthly = "Recurring"
}

struct CreateBookingView: View {

    // MARK: - Environment

    @Environment(\.dismiss) private var dismiss
    @Environment(UserRepository.self) private var userRepository
    @Environment(BookingRepository.self) private var bookingRepository
    @Environment(AssetRepository.self) private var assetRepository
    @Environment(AssetCategoryRepository.self) private var assetCategoryRepository

    // MARK: - Constants

    let assetId: Int64

    private static var calendar: Calendar {
        var calendar = Calendar(identifier: .gregorian)
        calendar.timeZone = .current
        return calendar
    }

    private let tabs = [
        "Create Booking",
        "Check Availability"
    ]

    // MARK: - State
    
    @State private var recurringMode: RecurringMode = .range
    
    @State private var fromDate = Date.now
    @State private var toDate = Date.now
    @State private var bookingDate = Date.now
    
    @State private var selectedMonth: Date = Date.now
    @State private var selectedWeekdays: Set<Int> = []

    @State private var startHour = 9
    @State private var endHour = 10

    @State private var selectedTab = 0
    @State private var showSuccess = false
    
    @State private var notes: String = ""
    
    @State private var availabilityBookings: [BookingResponse] = []
    @State private var isLoadingAvailability = false
    
    private func loadAvailabilityBookings(assetId: Int64) async {
        isLoadingAvailability = true
        defer {
            isLoadingAvailability = false
        }

        let today = Self.calendar.startOfDay(for: Date())

        async let pending = bookingRepository.loadAssetBookings(
            assetId: assetId,
            status: BookingStatus.PENDING,
            bookingStart: today
        )

        async let approved = bookingRepository.loadAssetBookings(
            assetId: assetId,
            status: BookingStatus.APPROVED,
            bookingStart: today
        )

        let result = await (
            pending,
            approved
        )

        availabilityBookings = result.0 + result.1
    }

    // MARK: - Data

    private var asset: AssetResponse? {
        assetRepository.asset(id: assetId)
    }

    private var user: UserResponse? {
        userRepository.loggedInUser
    }

    private var assetCategory: AssetCategoryResponse? {
        guard let asset else { return nil }

        return assetCategoryRepository.assetCategory(
            id: asset.categoryId
        )
    }

    private var bookingMode: BookingMode {
        assetCategory?.bookingMode ?? .daily
    }

    // MARK: - Booking Dates
    
    private static var utcCalendar: Calendar = {
        var calendar = Calendar(identifier: .gregorian)
        calendar.timeZone = TimeZone(secondsFromGMT: 0)!
        return calendar
    }()

    private func bookingDate(
        _ date: Date,
        hour: Int,
        minute: Int = 0
    ) -> Date {

        let components = Self.utcCalendar.dateComponents(
            [.year, .month, .day],
            from: date
        )

        return Self.utcCalendar.date(
            from: DateComponents(
                year: components.year,
                month: components.month,
                day: components.day,
                hour: hour,
                minute: minute
            )
        )!
    }

    private var bookingStart: Date {
        switch bookingMode {
        case .hourly:
            return bookingDate(
                bookingDate,
                hour: startHour
            )
            
        case .daily, .recurring:
            if Self.calendar.isDateInToday(fromDate) {
                let now = Date()
                let components = Self.calendar.dateComponents([.hour, .minute], from: now)
                
                let nextHour = (components.minute ?? 0) == 0
                ? (components.hour ?? 0)
                : (components.hour ?? 0) + 1
                
                return bookingDate(
                    fromDate,
                    hour: min(nextHour, 23)
                )
            }
            
            return bookingDate(
                fromDate,
                hour: 0
            )
        }
    }

    private var bookingEnd: Date {
        switch bookingMode {
        case .hourly:
            return bookingDate(
                bookingDate,
                hour: endHour
            )
        case .daily:
            return bookingDate(
                toDate,
                hour: 23,
                minute: 59
            )
        case .recurring:
            return bookingDate(
                toDate,
                hour: 22
            )
        }
    }
    
    private var successFromDate: Date {
        if recurringMode == .monthly {
            return recurringTimeSlots()
                .compactMap { ISO8601DateFormatter().date(from: $0.bookingStart) }
                .min() ?? fromDate
        }

        return bookingStart
    }

    private var successToDate: Date {
        if recurringMode == .monthly {
            return recurringTimeSlots()
                .compactMap { ISO8601DateFormatter().date(from: $0.bookingEnd) }
                .max() ?? toDate
        }

        return bookingEnd
    }
    
    private func recurringTimeSlots() -> [TimeSlotRequest] {
        let calendar = Self.calendar

        guard let month = calendar.dateInterval(
            of: .month,
            for: selectedMonth
        ) else {
            return []
        }

        let tomorrow = calendar.startOfDay(for: Date())

        var slots: [TimeSlotRequest] = []

        var date = month.start

        while date < month.end {

            let day = calendar.startOfDay(for: date)

            if day > tomorrow {

                let weekday = calendar.component(
                    .weekday,
                    from: day
                )

                if selectedWeekdays.contains(weekday) {

                    let components = calendar.dateComponents(
                        [.year, .month, .day],
                        from: day
                    )

                    let start = Self.utcCalendar.date(
                        from: DateComponents(
                            year: components.year,
                            month: components.month,
                            day: components.day,
                            hour: 6,
                            minute: 0
                        )
                    )!

                    let end = Self.utcCalendar.date(
                        from: DateComponents(
                            year: components.year,
                            month: components.month,
                            day: components.day,
                            hour: 22,
                            minute: 0
                        )
                    )!

                    slots.append(
                        TimeSlotRequest(
                            bookingStart: start.ISO8601Format(),
                            bookingEnd: end.ISO8601Format()
                        )
                    )
                }
            }

            date = calendar.date(
                byAdding: .day,
                value: 1,
                to: date
            )!
        }

        return slots
    }
    
    private var unavailableHours: Set<Int> {
        guard bookingMode == .hourly else {
            return []
        }

        return unavailableHours(for: bookingDate)
    }

    private func unavailableHours(
        for date: Date
    ) -> Set<Int> {

        var result: Set<Int> = []

        for booking in availabilityBookings {

            guard Self.calendar.isDate(
                booking.bookingStart,
                inSameDayAs: date
            ) else {
                continue
            }

            let start = Self.utcCalendar.component(
                .hour,
                from: booking.bookingStart
            )

            let end = Self.utcCalendar.component(
                .hour,
                from: booking.bookingEnd
            )

            for hour in start..<end {
                result.insert(hour)
            }
        }

        return result
    }
    
    // MARK: - Body
    var body: some View {
        ScreenContainer {
            if let asset, let assetCategory, let user {
                content(
                    asset: asset,
                    category: assetCategory,
                    user: user
                )
                .task {
                    await loadAvailabilityBookings(assetId: asset.id)
                }
            } else {
                unavailableView
            }
        }
        .navigationTitle("Create Booking")
        .sheet(isPresented: $showSuccess) {
            if let asset, let assetCategory {
                BookingSuccessView(
                    asset: asset,
                    category: assetCategory,
                    fromDate: successFromDate,
                    toDate: successToDate
                ) {
                    dismiss()
                }
            }
        }
    }

    // MARK: - Content
    @ViewBuilder
    private var tabsPicker: some View {
        Picker(
            "Bookings",
            selection: $selectedTab
        ) {

            ForEach(tabs.indices, id: \.self) { index in

                Text(tabs[index])
                    .tag(index)
            }
        }
        .pickerStyle(.segmented)
        .padding(.horizontal)
    }

    @ViewBuilder
    private func content(
        asset: AssetResponse,
        category: AssetCategoryResponse,
        user: UserResponse
    ) -> some View {
        
        tabsPicker
        
        switch selectedTab {
        case 0:
            bookingContent(
                asset: asset,
                user: user
            )
        default:
            AvailabilityCalendarView(bookings: availabilityBookings)
        }
    }

    @ViewBuilder
    private func bookingContent(
        asset: AssetResponse,
        user: UserResponse
    ) -> some View {
        InfoRow(
            label: "Asset",
            value: asset.name
        )
        BookingPeriodSection(
            mode: bookingMode,
            fromDate: $fromDate,
            toDate: $toDate,
            bookingDate: $bookingDate,
            startHour: $startHour,
            endHour: $endHour,
            selectedMonth: $selectedMonth,
            selectedWeekdays: $selectedWeekdays,
            recurringMode: $recurringMode,
            unavailableHours: unavailableHours
        )
        bookingButton(
            asset: asset,
            user: user
        )
    }

    // MARK: - Actions
    @ViewBuilder
    private func bookingButton(
        asset: AssetResponse,
        user: UserResponse
    ) -> some View {
        Section {
            if !showSuccess {
                Button {
                    Task {
                        let success: Bool

                        if recurringMode == .monthly {
                            success = await bookingRepository.createRecurringBooking(
                                userId: user.id,
                                assetId: asset.id,
                                timeSlots: recurringTimeSlots(),
                                selectedWeekdays: selectedWeekdays,
                                notes: notes
                            )
                        } else {
                            success = await bookingRepository.createBooking(
                                userId: user.id,
                                assetId: asset.id,
                                bookingStart: bookingStart,
                                bookingEnd: bookingEnd,
                                notes: notes
                            )
                        }

                        if success {
                            showSuccess = true
                        }
                    }
                } label: {
                    HStack {
                        if bookingRepository.loadState == .loading {

                            ProgressView()
                                .tint(.white)
                        } else {
                            Text("Confirm Booking")
                        }
                    }
                    .frame(maxWidth: .infinity)
                }
                .buttonStyle(.borderedProminent)
                .disabled(
                    bookingRepository.loadState == .loading
                )
            }
        }
    }

    // MARK: - Empty State
    @ViewBuilder
    private var unavailableView: some View {
        ContentUnavailableView {
            Label(
                "Could Not Load Data",
                systemImage: "exclamationmark.triangle"
            )
        } description: {
            Text(
                "The asset information could not be loaded."
            )
        } actions: {
            Button("Return") {
                dismiss()
            }
            .buttonStyle(.borderedProminent)
        }
    }
}
