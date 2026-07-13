//
//  ApprovalBookingView.swift
//  Asset Booking Management
//
//  Created by Jakov  Petric on 07.07.2026..
//

import SwiftUI

struct ApprovalBookingView: View {
    @Environment(BookingRepository.self) private var bookingRepository
    @Environment(UserRepository.self) private var userRepository

    private var user: UserResponse? {
        userRepository.loggedInUser
    }
    
    private var filteredBookings: [BookingResponse] {
        guard let user else { return [] }
        
        if (user.role == .ADMIN) {
            return bookingRepository
                .filterBookingsByStatus(status: .PENDING)
                .filter { $0.bookingStart > Date() }
                .sorted { $0.bookingStart < $1.bookingStart }
        }
        
        return bookingRepository
            .filterBookingsByStatus(status: .PENDING)
            .filter { $0.user.managerEmail == user.email }
            .filter { $0.bookingStart > Date() }
            .sorted { $0.bookingStart < $1.bookingStart }
    }

    var body: some View {
        ScreenContainer{
            if bookingRepository.loadState == .loading {
                ProgressView()
            } else {
                ScrollView {
                    LazyVStack(spacing: 12) {
                        if filteredBookings.isEmpty {
                            Text("No bookings found")
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
        .navigationTitle("Pending approvals")
    }
}

#Preview {
    ApprovalBookingView()
}
