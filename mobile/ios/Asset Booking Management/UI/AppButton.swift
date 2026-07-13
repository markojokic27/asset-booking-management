//
//  AppButton.swift
//  Asset Booking Management
//
//  Created by Jakov  Petric on 17.06.2026..
//

import Foundation
import SwiftUI

enum AppButtonVariant {
    case primary
    case secondary
    case danger
    case success

    var background: Color {
        switch self {
            case .primary: .accentColor
            case .secondary: Color.gray
            case .danger: Color.red
            case .success: Color.green
        }
    }
}

struct AppButtonStyle: ButtonStyle {
    let variant: AppButtonVariant

    func makeBody(configuration: Configuration) -> some View {
        configuration.label
            .padding(.vertical, 10)
            .padding(.horizontal, 16)
            .bold()
            .foregroundStyle(.white)
            .background(variant.background)
            .clipShape(Capsule())
            .scaleEffect(configuration.isPressed ? 0.95 : 1)
            .animation(.easeOut(duration: 0.15), value: configuration.isPressed)
    }
}

struct AppButton<Content: View>: View {
    let action: () -> Void
    let isLoading: Bool
    let variant: AppButtonVariant
    let content: () -> Content

    init(
        isLoading: Bool = false,
        variant: AppButtonVariant = .primary,
        action: @escaping () -> Void,
        @ViewBuilder content: @escaping () -> Content
    ) {
        self.isLoading = isLoading
        self.variant = variant
        self.action = action
        self.content = content
    }

    var body: some View {
        Button(action: action) {
            if isLoading {
                ProgressView()
            } else {
                content()
            }
        }
        .buttonStyle(AppButtonStyle(variant: variant))
        .disabled(isLoading)
    }
}

#Preview {
    AppButton(action: {}) {
        Text("LOGIN").fontWeight(.bold)
    }
}
