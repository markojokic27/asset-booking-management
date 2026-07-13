//
//  UserService.swift
//  Asset Booking Management
//
//  Created by Jakov  Petric on 04.07.2026..
//

import Foundation

protocol UserServiceProtocol {
    func getUserById(id: Int) async throws -> UserResponse
    func changePassword(id: Int64, currentPassword: String, newPassword: String) async throws -> Void
}

final class UserService: UserServiceProtocol {
    
    private let api: APIClient
    
    init(api: APIClient) {
        self.api = api
    }
    
    func getUserById(id: Int) async throws -> UserResponse {
        try await api.request(UserEndpoint.getUserById(id: id))
    }
    
    func changePassword(id: Int64, currentPassword: String, newPassword: String) async throws -> Void {
        try await api.request(UserEndpoint.changePassword(id: id, currentPassword: currentPassword, newPassword: newPassword))
    }
}
