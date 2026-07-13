//
//  User.swift
//  Asset Booking Management
//
//  Created by Jakov  Petric on 18.06.2026..
//

import Foundation

enum Role: String, Codable, CaseIterable, Identifiable {
    case ADMIN
    case MANAGER
    case EMPLOYEE

    var id: Self { self }

    var canManageApprovals: Bool {
        switch self {
        case .ADMIN, .MANAGER:
            return true
        case .EMPLOYEE:
            return false
        }
    }
}

enum Status: String, Codable, CaseIterable, Identifiable {
    case ACTIVE
    case INACTIVE
    case STUDENT
    case LEFT_COMPANY
    case DELETED
    
    var id: Self { self }
}

struct UserResponse: Codable, Identifiable {
    let id: Int64
    let name: String
    let surname: String
    let username: String
    let email: String
    let role: Role
    let status: Status
    let departmentId: Int64
    let managerEmail: String?
    let notes: String
}

struct PasswordChangeRequest: Codable {
    let currentPassword: String
    let newPassword: String
}

extension UserResponse {
    static var preview: UserResponse {
        return UserResponse(
            id: 1,
            name: "Name",
            surname: "Surname",
            username: "user_admin",
            email: "email@example.com",
            role: .ADMIN,
            status: .ACTIVE,
            departmentId: 1,
            managerEmail: "Manager@example.com",
            notes: "Dummy notes"
        )
    }
}
