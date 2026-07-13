//
//  StatusBadge.swift
//  Asset Booking Management
//
//  Created by Jakov  Petric on 18.06.2026..
//

import SwiftUI

struct StatusStyle {
    let background: Color
    let foreground: Color
    let border: Color
}

struct StatusBadge: View {
    @Environment(\.colorScheme) private var colorScheme

    let status: String

    private var style: StatusStyle {
        let isDark = colorScheme == .dark

        switch status.uppercased() {
        case "ACTIVE", "APPROVED":
            return isDark
                ? StatusStyle(
                    background: .green.opacity(0.2),
                    foreground: .green,
                    border: .green.opacity(0.6)
                )
                : StatusStyle(
                    background: .green.opacity(0.1),
                    foreground: .green,
                    border: .green.opacity(0.4)
                )

        case "INACTIVE", "REJECTED", "CANCELLED":
            return isDark
                ? StatusStyle(
                    background: .red.opacity(0.2),
                    foreground: .red,
                    border: .red.opacity(0.6)
                )
                : StatusStyle(
                    background: .red.opacity(0.1),
                    foreground: .red,
                    border: .red.opacity(0.4)
                )

        case "DAMAGED", "PENDING":
            return isDark
                ? StatusStyle(
                    background: .orange.opacity(0.2),
                    foreground: .orange,
                    border: .orange.opacity(0.6)
                )
                : StatusStyle(
                    background: .orange.opacity(0.1),
                    foreground: .orange,
                    border: .orange.opacity(0.4)
                )

        case "DELETED":
            return isDark
                ? StatusStyle(
                    background: .gray.opacity(0.2),
                    foreground: .gray,
                    border: .gray.opacity(0.6)
                )
                : StatusStyle(
                    background: .gray.opacity(0.1),
                    foreground: .gray,
                    border: .gray.opacity(0.4)
                )

        case "COMPLETED":
            return isDark
                ? StatusStyle(
                    background: .blue.opacity(0.2),
                    foreground: .blue,
                    border: .blue.opacity(0.6)
                )
                : StatusStyle(
                    background: .blue.opacity(0.1),
                    foreground: .blue,
                    border: .blue.opacity(0.4)
                )

        default:
            return isDark
                ? StatusStyle(
                    background: .secondary.opacity(0.2),
                    foreground: .secondary,
                    border: .secondary.opacity(0.6)
                )
                : StatusStyle(
                    background: .secondary.opacity(0.1),
                    foreground: .secondary,
                    border: .secondary.opacity(0.4)
                )
        }
    }

    var body: some View {
        Text(status)
            .font(.caption)
            .fontWeight(.medium)
            .foregroundStyle(style.foreground)
            .padding(.horizontal, 8)
            .padding(.vertical, 4)
            .background(style.background)
            .overlay {
                Capsule()
                    .stroke(style.border, lineWidth: 1)
            }
            .clipShape(Capsule())
    }
}

#Preview {
    StatusBadge(status: "ACTIVE")
}
