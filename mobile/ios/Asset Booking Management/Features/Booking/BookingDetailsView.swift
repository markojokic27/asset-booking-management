//
//  BookingDetails.swift
//  Asset Booking Management
//
//  Created by Jakov  Petric on 07.07.2026..
//

import SwiftUI

struct BookingDetailsView: View {
    let bookingId: Int64
    @State private var requestInProgress = false
    
    @Environment(UserRepository.self) private var userRepository
    @Environment(BookingRepository.self) private var bookingRepository

    private var booking: BookingResponse? {
        bookingRepository.booking(id: bookingId)
    }
    
    private var user: UserResponse? {
        userRepository.loggedInUser
    }
    
    private var canManageBooking: Bool {
        guard let booking, let user else { return false }

        guard booking.status == .PENDING else {
            return false
        }

        guard booking.bookingEnd > Date() else {
            return false
        }

        return user.role == .ADMIN ||
            booking.user.managerEmail == user.email
    }

    private var canCompleteBooking: Bool {
        guard let booking, let user else { return false }

        guard booking.status == .APPROVED else {
            return false
        }

        guard booking.bookingEnd > Date() else {
            return false
        }

        return user.role == .ADMIN ||
            booking.user.email == user.email
    }

    private var canCancelBooking: Bool {
        guard let booking, let user else { return false }

        guard ![.CANCELLED, .COMPLETED, .REJECTED].contains(booking.status) else {
            return false
        }

        guard booking.bookingEnd > Date() else {
            return false
        }

        return booking.user.id == user.id
    }

    var body: some View {
        ScreenContainer {
            if let booking {
                CardView {
                    VStack(alignment: .leading, spacing: 0) {

                        VStack(alignment: .leading, spacing: 4) {
                            Text(booking.asset.name)
                                .font(.title2)
                                .fontWeight(.semibold)
                        }
                        .padding(.bottom, 16)
                        
                        InfoRow(
                            label: "User",
                            value: booking.user.full_name
                        )

                        Divider()

                        InfoRow(
                            label: "From",
                            value: booking.bookingStart.formatted(date: .abbreviated, time: .shortened)
                        )

                        Divider()

                        InfoRow(
                            label: "To",
                            value: booking.bookingEnd.formatted(date: .abbreviated, time: .shortened)
                        )

                        Divider()

                        HStack {
                            Text("Status")

                            Spacer()

                            StatusBadge(status: booking.status.rawValue)
                        }
                        .padding(.vertical, 12)

                        Divider()

                        InfoRow(
                            label: "Category",
                            value: booking.asset.category.name
                        )
                        
                        if canManageBooking {
                            
                            Divider()
                            
                            HStack(spacing: 12) {
                                Button {
                                    Task {
                                        requestInProgress = true
                                        defer { requestInProgress = false }

                                        await bookingRepository.approveBooking(id: booking.id)
                                    }
                                } label: {
                                    if requestInProgress {
                                        ProgressView()
                                            .tint(.white)
                                    } else {
                                        Text("Approve")
                                    }
                                }
                                .buttonStyle(.borderedProminent)
                                .tint(.green)
                                .disabled(requestInProgress)

                                Button {
                                    Task {
                                        requestInProgress = true
                                        defer { requestInProgress = false }

                                        await bookingRepository.rejectBooking(id: booking.id)
                                    }
                                } label: {
                                    if requestInProgress {
                                        ProgressView()
                                            .tint(.white)
                                    } else {
                                        Text("Reject")
                                    }
                                }
                                .buttonStyle(.borderedProminent)
                                .tint(.red)
                                .disabled(requestInProgress)
                            }
                            .frame(maxWidth: .infinity)
                            .padding()
                        }
                        
                        if canCompleteBooking {
                            
                            Divider()
                            
                            HStack {
                                Spacer()

                                Button {
                                    Task {
                                        requestInProgress = true
                                        defer { requestInProgress = false }

                                        await bookingRepository.updateBooking(
                                            id: booking.id,
                                            status: .COMPLETED
                                        )
                                    }
                                } label: {
                                    HStack {
                                        if requestInProgress {
                                            ProgressView()
                                                .tint(.white)
                                        } else {
                                            Text("Complete Booking")
                                        }
                                    }
                                    .frame(minWidth: 120)
                                }
                                .buttonStyle(.borderedProminent)
                                .tint(.blue)
                                .disabled(requestInProgress)

                                Spacer()
                            }
                            .padding()
                        }

                        if canCancelBooking {
                            
                            Divider()
                            
                            HStack {
                                Spacer()

                                Button {
                                    Task {
                                        requestInProgress = true
                                        defer { requestInProgress = false }

                                        await bookingRepository.updateBooking(
                                            id: booking.id,
                                            status: .CANCELLED
                                        )
                                    }
                                } label: {
                                    HStack {
                                        if requestInProgress {
                                            ProgressView()
                                                .tint(.white)
                                        } else {
                                            Text("Cancel Booking")
                                        }
                                    }
                                    .frame(minWidth: 120)
                                }
                                .buttonStyle(.borderedProminent)
                                .tint(.red)
                                .disabled(requestInProgress)

                                Spacer()
                            }
                            .padding()
                        }
                        
                    }
                    .padding()
                }
            } else {
                ProgressView()
            }

            Spacer()
        }
        .navigationTitle("Booking details")
    }
}

