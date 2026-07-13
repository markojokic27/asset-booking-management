//
//  ErrorHandler.swift
//  Asset Booking Management
//
//  Created by Jakov  Petric on 07.07.2026..
//

import Foundation

@MainActor
@Observable
final class ErrorView {
    var error: Error?

    var isPresented: Bool {
        error != nil
    }

    func show(_ error: Error) {
        self.error = error
    }

    func dismiss() {
        error = nil
    }
}
