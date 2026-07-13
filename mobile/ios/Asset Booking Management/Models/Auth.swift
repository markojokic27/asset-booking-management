//
//  Auth.swift
//  Asset Booking Management
//
//  Created by Jakov  Petric on 30.06.2026..
//

import Foundation

struct LoginRequest: Codable {
    let username: String
    let password: String
}

struct LoginResponse: Codable {
    let accessToken: String
    let refreshToken: String
}

struct RefreshRequest: Codable {
    let refreshToken: String
}
