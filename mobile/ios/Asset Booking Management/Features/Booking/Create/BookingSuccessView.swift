//
//  BookingSuccessView.swift
//  Asset Booking Management
//
//  Created by Jakov  Petric on 10.07.2026..
//

import SwiftUI

struct BookingSuccessView: View {
    @Environment(\.dismiss) private var dismiss

    let asset: AssetResponse
    let category: AssetCategoryResponse
    let fromDate: Date
    let toDate: Date
    
    let onDone: () -> Void

    private var formatter: DateFormatter {
        let formatter = DateFormatter()
        formatter.dateStyle = .medium
        formatter.timeStyle = .short
        formatter.timeZone = TimeZone(secondsFromGMT: 0)
        return formatter
    }

    var body: some View {
        NavigationStack {
            VStack(spacing: 24) {
                Image(systemName: "checkmark.circle.fill")
                    .font(.system(size: 72))
                    .foregroundStyle(.green)

                Text("Booking Created")
                    .font(.title2)
                    .fontWeight(.semibold)

                CardView {
                    VStack(alignment: .leading, spacing: 12) {
                        InfoRow(
                            label: "Asset",
                            value: asset.name
                        )

                        Divider()

                        InfoRow(
                            label: "Category",
                            value: category.name
                        )

                        Divider()

                        InfoRow(
                            label: "From",
                            value: formatter.string(from: fromDate)
                        )

                        Divider()

                        InfoRow(
                            label: "To",
                            value: formatter.string(from: toDate)
                        )
                    }
                    .padding()
                }

                Button("Done") {
                    dismiss()

                    DispatchQueue.main.async {
                        onDone()
                    }
                }
                .buttonStyle(.borderedProminent)

                Spacer()
            }
            .padding()
            .navigationTitle("Success")
            .navigationBarTitleDisplayMode(.inline)
        }
    }
}
