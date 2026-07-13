//
//  CardView.swift
//  Asset Booking Management
//
//  Created by Jakov  Petric on 18.06.2026..
//

import SwiftUI

struct CardView<Content: View>: View {
    @ViewBuilder let content: Content
    
    var body: some View {
            content
                .background(
                    RoundedRectangle(cornerRadius: 16)
                        .fill(Color(.systemBackground))
                )
                .overlay(
                    RoundedRectangle(cornerRadius: 16)
                        .stroke(Color.gray.opacity(0.15), lineWidth: 1)
                )
                .shadow(color: Color.black.opacity(0.08), radius: 4, y: 2)
        }
}
