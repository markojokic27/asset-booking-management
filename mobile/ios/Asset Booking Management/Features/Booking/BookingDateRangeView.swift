//
//  BookingDateRangeView.swift
//  Asset Booking Management
//
//  Created by Jakov  Petric on 10.07.2026..
//

import SwiftUI

struct BookingDateRangeView: View {
    @Environment(\.dismiss) private var dismiss

    @Binding var bookingStart: Date
    @Binding var bookingEnd: Date

    let onConfirm: () -> Void

    var body: some View {
        NavigationStack {
            Form {
                Section("Booking Range") {

                    DatePicker(
                        "From",
                        selection: $bookingStart,
                        displayedComponents: .date
                    )

                    DatePicker(
                        "To",
                        selection: $bookingEnd,
                        in: bookingStart...,
                        displayedComponents: .date
                    )
                }
            }
            .navigationTitle("Load Bookings")
            .toolbar {
                ToolbarItem(placement: .topBarLeading) {
                    Button("Cancel") {
                        dismiss()
                    }
                }

                ToolbarItem(placement: .topBarTrailing) {
                    Button("Load") {
                        onConfirm()
                    }
                }
            }
        }
    }
}
