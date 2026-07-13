//
//  BookingCard.swift
//  Asset Booking Management
//
//  Created by Jakov  Petric on 18.06.2026..
//

import Foundation
import SwiftUI

struct BookingCard: View {
    let bookingId: Int64

    @Environment(BookingRepository.self) private var bookingRepository

    private var booking: BookingResponse? {
        bookingRepository.booking(id: bookingId)
    }

    var body: some View {
        if let booking {
            NavigationLink(destination: BookingDetailsView(bookingId: bookingId)) {
                CardView {
                    HStack(alignment: .center) {
                        VStack(alignment: .leading, spacing: 4) {
                            Text(booking.asset.name)
                                .font(.body)
                                .fontWeight(.bold)
                                .foregroundStyle(.primary)

                            Text(
                                "\(booking.bookingStart.formatted(date: .abbreviated, time: .shortened)) - \(booking.bookingEnd.formatted(date: .abbreviated, time: .shortened))"
                            )
                            .font(.subheadline)
                            .foregroundStyle(.primary)
                        }

                        Spacer()

                        StatusBadge(status: booking.status.rawValue)
                    }
                    .padding(16)
                    .frame(maxWidth: .infinity, alignment: .leading)
                }
            }
        }
    }
}
