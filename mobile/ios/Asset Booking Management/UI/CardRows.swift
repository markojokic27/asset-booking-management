//
//  CardRows.swift
//  Asset Booking Management
//
//  Created by Jakov  Petric on 18.06.2026..
//

import SwiftUI

struct InfoRow: View {
    let label: String
    let value: String
    
    var body: some View {
        HStack {
            Text(label)
                .foregroundStyle(.primary)
            
            Spacer()
            
            Text(value)
        }
        .padding(.vertical, 12)
    }
}

struct BadgeInfoRow<Content: View>: View {
    let label: String
    @ViewBuilder let content: Content

    var body: some View {
        HStack {
            Text(label)
                .foregroundStyle(.primary)

            Spacer()

            content
        }
        .padding(.vertical, 12)
    }
}
