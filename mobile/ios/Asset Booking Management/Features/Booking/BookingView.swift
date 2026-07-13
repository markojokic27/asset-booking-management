//
//  BookingView.swift
//  Asset Booking Management
//
//  Created by Jakov  Petric on 17.06.2026..
//

import Foundation
import SwiftUI

struct BookingView: View {
    @Environment(UserRepository.self) private var userRepository
    @Environment(BookingRepository.self) private var bookingRepository

    @State private var selectedTab = 0
    @State private var searchText = ""
    @State private var showingFilters = false
    @State private var selectedStatus: BookingStatus?
    @State private var selectedCategoryId: Int64?
    @State private var showingDateRange = false

    private let tabs = ["My active bookings", "History"]

    private var userId: Int64? {
        userRepository.loggedInUser?.id
    }

    private var myBookings: [BookingResponse] {
        guard let userId else { return [] }

        return bookingRepository.bookings.values
            .filter { $0.user.id == userId }
            .sorted { $0.bookingStart > $1.bookingStart }
    }

    private var tabBookings: [BookingResponse] {
        switch selectedTab {
        case 0:
            return myBookings.filter { [.PENDING, .APPROVED].contains($0.status) }
        case 1:
            return myBookings.filter { [.COMPLETED, .CANCELLED, .REJECTED].contains($0.status) }
        default:
            return []
        }
    }

    private var filteredBookings: [BookingResponse] {
        var bookings = tabBookings

        if !searchText.isEmpty {
            let query = searchText.localizedLowercase

            bookings = bookings.filter { booking in
                booking.asset.name.localizedLowercase.contains(query) ||
                booking.asset.location.localizedLowercase.contains(query) ||
                booking.asset.category.name.localizedLowercase.contains(query) ||
                booking.user.name.localizedLowercase.contains(query) ||
                booking.user.surname.localizedLowercase.contains(query) ||
                booking.user.full_name.localizedLowercase.contains(query) ||
                (booking.notes?.localizedLowercase.contains(query) ?? false)
            }
        }

        if let status = selectedStatus {
            bookings = bookings.filter { $0.status == status }
        }

        if let categoryId = selectedCategoryId {
            bookings = bookings.filter { $0.asset.category.id == categoryId }
        }

        return bookings
    }
    
    private var availableCategories: [BookingCategorySummary] {
        Dictionary(
            grouping: myBookings,
            by: { $0.asset.category.id }
        )
        .compactMap { $0.value.first?.asset.category }
        .sorted { $0.name < $1.name }
    }

    @ViewBuilder
    private var bookingTabs: some View {
        Picker("Bookings", selection: $selectedTab) {
            ForEach(tabs.indices, id: \.self) { index in
                Text(tabs[index])
                    .tag(index)
            }
        }
        .pickerStyle(.segmented)
        .padding(.horizontal)
    }

    var body: some View {
        ZStack(alignment: .bottomTrailing) {
            ScreenContainer {
                bookingTabs
                
                if bookingRepository.loadState == .loading {
                    ProgressView()
                } else {
                    ScrollView {
                        LazyVStack(spacing: 12) {
                            if filteredBookings.isEmpty {
                                Text("No bookings found")
                                    .foregroundStyle(.secondary)
                                    .padding()
                            } else {
                                ForEach(filteredBookings) { booking in
                                    BookingCard(bookingId: booking.id)
                                }
                            }
                        }
                        .padding(.top, 12)
                    }
                    .scrollIndicators(.hidden)
                }
                
                Spacer(minLength: 0)
            }
            .navigationTitle(TabItem.booking.title)
            .searchable(text: $searchText, prompt: "Search bookings")
            .toolbar {
                ToolbarItem(placement: .topBarTrailing) {
                    Button {
                        showingFilters = true
                    } label: {
                        Image(systemName: "line.3.horizontal.decrease.circle")
                    }
                }
            }
            .sheet(isPresented: $showingDateRange) {
                @Bindable var bookingRepository = bookingRepository
                
                BookingDateRangeView(
                    bookingStart: $bookingRepository.bookingStart,
                    bookingEnd: $bookingRepository.bookingEnd
                ) {
                    Task {
                        await bookingRepository.loadBookings(
                            bookingStart: bookingRepository.bookingStart,
                            bookingEnd: bookingRepository.bookingEnd
                        )

                        showingDateRange = false
                    }
                }
            }
            .sheet(isPresented: $showingFilters) {
                NavigationStack {
                    Form {
                        
                        Section("Status") {
                            Picker("Status", selection: $selectedStatus) {
                                Text("All").tag(nil as BookingStatus?)
                                
                                ForEach(BookingStatus.allCases) { status in
                                    Text(status.rawValue.capitalized)
                                        .tag(Optional(status))
                                }
                            }
                        }
                        
                        Section("Asset Category") {
                            Picker("Category", selection: $selectedCategoryId) {
                                Text("All").tag(nil as Int64?)
                                
                                ForEach(availableCategories) { category in
                                    Text(category.name)
                                        .tag(Optional(category.id))
                                }
                            }
                        }
                    }
                    .navigationTitle("Filters")
                    .toolbar {
                        
                        ToolbarItem(placement: .topBarLeading) {
                            Button("Clear") {
                                selectedStatus = nil
                                selectedCategoryId = nil
                            }
                        }
                        
                        ToolbarItem(placement: .topBarTrailing) {
                            Button("Done") {
                                showingFilters = false
                            }
                        }
                    }
                }
            }
            
            Button {
                showingDateRange = true
            } label: {
                Image(systemName: "calendar")
                    .font(.title2)
                    .foregroundStyle(.white)
                    .padding()
                    .background(Color.accentColor)
                    .clipShape(Circle())
                    .shadow(radius: 4)
            }
            .padding()
        }
        .refreshable {
            @Bindable var bookingRepository = bookingRepository
            
            await bookingRepository.loadBookings(
                bookingStart: bookingRepository.bookingStart,
                bookingEnd: bookingRepository.bookingEnd
            )
        }
    }
}

#Preview{
    BookingView()
}
