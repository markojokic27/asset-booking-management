//
//  Toast.swift
//  Asset Booking Management
//
//  Created by Jakov  Petric on 08.07.2026..
//

import Foundation
import SwiftUI
import Observation

@MainActor
@Observable
final class ToastManager {
    var toast: Toast?

    func show(
        _ message: String,
        style: ToastStyle = .success
    ) {
        let toast = Toast(
            message: message,
            style: style
        )

        self.toast = toast

        Task {
            try? await Task.sleep(for: .seconds(2))

            if self.toast?.id == toast.id {
                self.toast = nil
            }
        }
    }
}

enum ToastStyle {
    case success
    case error
    case warning
    case info
}

struct Toast: Identifiable {
    let id = UUID()
    let message: String
    let style: ToastStyle
}

struct ToastView: View {
    let toast: Toast

    var body: some View {
        HStack(spacing: 12) {
            Image(systemName: icon)

            Text(toast.message)
                .font(.subheadline)
                .multilineTextAlignment(.leading)
        }
        .foregroundStyle(.white)
        .padding(.horizontal, 16)
        .padding(.vertical, 12)
        .frame(maxWidth: .infinity)
        .background(color)
        .clipShape(RoundedRectangle(cornerRadius: 14))
        .shadow(radius: 6)
    }

    private var color: Color {
        switch toast.style {
        case .success: return .green
        case .error: return .red
        case .warning: return .orange
        case .info: return .blue
        }
    }

    private var icon: String {
        switch toast.style {
        case .success: return "checkmark.circle.fill"
        case .error: return "xmark.circle.fill"
        case .warning: return "exclamationmark.triangle.fill"
        case .info: return "info.circle.fill"
        }
    }
}
