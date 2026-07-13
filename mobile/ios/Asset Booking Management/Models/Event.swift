//
//  Event.swift
//  Asset Booking Management
//
//  Created by Jakov  Petric on 06.07.2026..
//

import Foundation

enum AppEvent: Sendable {
    case auth(AuthEvent)
    case user(UserEvent)
}

enum AuthEvent: Sendable {
    case loggedIn(userId: Int)
    case loggedOut
}

enum UserEvent: Sendable {
    case passwordChanged(username: String, newPassword: String)
}
