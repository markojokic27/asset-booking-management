//
//  HomeView.swift
//  Asset Booking Management
//
//  Created by Jakov  Petric on 17.06.2026..
//

import Foundation
import SwiftUI

struct HomeView: View {
    @Environment(UserRepository.self) private var userRepository
    @Environment(AssetRepository.self) private var assetRepository
    @Environment(BookingRepository.self) private var bookingRepository
    
    @Binding var tabSelection: TabItem

    init(tabSelection: Binding<TabItem>) {
        _tabSelection = tabSelection
    }
    
    private var user: UserResponse? {
        userRepository.loggedInUser
    }
    
    private var approvalBookingsCount: Int {
        guard let user else { return 0 }
        
        if (user.role == .ADMIN) {
            return bookingRepository
                .filterBookingsByStatus(status: .PENDING)
                .filter { $0.bookingStart > Date() }
                .count
        }
        
        return bookingRepository
            .filterBookingsByStatus(status: .PENDING)
            .filter { $0.user.managerEmail == user.email }
            .filter { $0.bookingStart > Date() }
            .count
    }

    var body: some View {
        ScreenContainer {
            HStack(spacing: 16) {
                HomeCard(
                    icon: "desktopcomputer",
                    accentColor: .blue,
                    backgroundColor: .blue.opacity(0.1),
                    count: assetRepository.assets.count,
                    label: "All Assets",
                    action: {
                        tabSelection = TabItem.asset
                    }
                )

                HomeCard(
                    icon: "calendar",
                    accentColor: .green,
                    backgroundColor: .green.opacity(0.1),
                    count: userRepository.loggedInUser.map {
                        bookingRepository.userBookingCount(id: $0.id)
                    } ?? 0,
                    label: "My Bookings",
                    action: {
                        tabSelection = .booking
                    }
                )
            }

            
            if userRepository.showHomeViewPendingCard {
                NavigationLink(destination: ApprovalBookingView()) {
                    ApprovalRequestsCard(
                        pendingCount: approvalBookingsCount
                    )
                }
            }
            
            Spacer()
        }
        .navigationTitle(TabItem.home.title)
        .buttonStyle(.plain)
        .toolbar {
            ToolbarItem(placement: .topBarTrailing) {
                Button {
                    Task {
                        @Bindable var bookingRepository = bookingRepository
                        
                        await assetRepository.loadAssets()
                        await bookingRepository.loadBookings(
                            bookingStart: bookingRepository.bookingStart,
                            bookingEnd: bookingRepository.bookingEnd
                        )
                    }
                } label: {
                    Image(systemName: "arrow.trianglehead.2.counterclockwise")
                }
            }
        }
    }
}

#Preview{
    HomeView(tabSelection: .constant(TabItem.home))
}
