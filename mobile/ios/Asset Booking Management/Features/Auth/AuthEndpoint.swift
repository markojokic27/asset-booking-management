//
//  AuthEndpoint.swift
//  Asset Booking Management
//
//  Created by Jakov  Petric on 30.06.2026..
//

import Foundation

enum AuthEndpoint: Endpoint {
    case login(username: String, password: String)
    case logout
    case refresh(refreshToken: String)

    var path: String {
        switch self {
        case .login:
            "/auth/login"
        case .logout:
            ""
        case .refresh:
            "/auth/refresh"
        }
    }

    var method: HTTPMethod {
        switch self {
        case .login:
            .post
        case .logout:
            .post
        case .refresh:
                .post
        }
    }

    var headers: [String: String] {
        ["Content-Type": "application/json"]
    }

    var body: Data? {
        switch self {
        case let .login(username, password):
            return try? JSONEncoder().encode(
                LoginRequest(
                    username: username,
                    password: password
                )
            )

        case .logout:
            return nil
            
        case let .refresh(refreshToken):
            return try? JSONEncoder().encode(
                RefreshRequest(refreshToken: refreshToken)
            )
        }
    }
}
