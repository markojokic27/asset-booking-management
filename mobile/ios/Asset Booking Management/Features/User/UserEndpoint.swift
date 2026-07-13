//
//  UserEndpoint.swift
//  Asset Booking Management
//
//  Created by Jakov  Petric on 04.07.2026..
//

import Foundation

enum UserEndpoint: Endpoint {
    case getUserById(id: Int)
    case changePassword(id: Int64, currentPassword: String, newPassword: String)
    
    var path: String {
        switch self {
        case let .getUserById(id):
            "/users/\(id)"
        case let .changePassword(id, _, _):
            "/users/\(id)/password"
        }
    }
    
    var method: HTTPMethod {
        switch self {
            case .getUserById:
                .get
            case .changePassword:
                .patch
        }
    }
    
    var body: Data? {
        switch self {
        case let .changePassword(_, currentPassword, newPassword):
            return try? JSONEncoder().encode(PasswordChangeRequest(currentPassword: currentPassword, newPassword: newPassword))
        default:
            return nil
        }
    }
}
