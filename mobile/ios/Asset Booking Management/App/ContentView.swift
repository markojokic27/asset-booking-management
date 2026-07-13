//
//  ContentView.swift
//  Asset Booking Management
//
//  Created by Jakov  Petric on 17.06.2026..
//

import SwiftUI

struct ContentView: View {
    @Environment(ErrorView.self) private var errorView
    @Environment(AuthRepository.self) private var authRepository
    @Environment(ToastManager.self) private var toastManager

    var body: some View {
        ZStack {
            switch authRepository.authState {
            case .unauthenticated:
                LoginView()
                    .transition(.opacity)

            case .authenticated:
                MainView()
                    .transition(.opacity)
            }
        }
        .safeAreaInset(edge: .top) {
            if let toast = toastManager.toast {
                ToastView(toast: toast)
                    .padding(.horizontal)
                    .padding(.top, 8)
                    .transition(.move(edge: .top).combined(with: .opacity))
            }
        }
        .animation(.spring(duration: 0.35), value: toastManager.toast?.id)
        .alert(
            "Error",
            isPresented: Binding(
                get: { errorView.error != nil },
                set: { if !$0 { errorView.dismiss() } }
            )
        ) {
            Button("OK") {
                errorView.dismiss()
            }
        } message: {
            Text(errorView.error?.localizedDescription ?? "Unknown error")
        }
    }
}

#Preview{
    ContentView()
}
