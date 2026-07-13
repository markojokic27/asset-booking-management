//
//  HomeCard.swift
//  Asset Booking Management
//
//  Created by Jakov  Petric on 18.06.2026..
//

import Foundation
import SwiftUI

struct HomeCard: View {
    let icon: String
    let accentColor: Color
    let backgroundColor: Color
    let count: Int
    let label: String
    let action: () -> Void

    var body: some View {
        Button(action: action) {
            VStack(spacing: 20) {
                Image(systemName: icon)
                    .font(.title3)
                    .foregroundStyle(accentColor)

                HStack(alignment: .bottom) {
                    VStack(alignment: .leading, spacing: 4) {
                        Text("\(count)")
                            .font(.title2)
                            .fontWeight(.bold)
                            .foregroundStyle(accentColor)

                        Text(label)
                            .font(.caption)
                            .foregroundStyle(.primary)
                    }

                    Spacer()

                    Image(systemName: "arrow.right")
                }
            }
            .padding()
            .background(backgroundColor)
            .clipShape(RoundedRectangle(cornerRadius: 14))
        }
        .buttonStyle(.plain)
    }
}

#Preview {
    HomeCard(
        icon: "desktopcomputer",
        accentColor: .blue,
        backgroundColor: .blue.opacity(0.1),
        count: 5,
        label: "All Assets",
        action: {}
    )
}
