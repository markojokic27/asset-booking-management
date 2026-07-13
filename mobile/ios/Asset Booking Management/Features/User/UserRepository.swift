//
//  UserRepository.swift
//  Asset Booking Management
//
//  Created by Jakov  Petric on 04.07.2026..
//

import Foundation
import Observation

@MainActor
@Observable
final class UserRepository {

    private let userService: UserServiceProtocol
    private let eventBus: EventBus
    private let errorView: ErrorView

    private var users: [Int: UserResponse] = [:]

    private(set) var loggedInUser: UserResponse?
    private(set) var loadState: LoadState = .idle

    private var eventTask: Task<Void, Never>?

    init(userService: UserServiceProtocol, eventBus: EventBus, errorView: ErrorView) {
        self.userService = userService
        self.eventBus = eventBus
        self.errorView = errorView

        startListening()
    }
    
    var showHomeViewPendingCard: Bool {
        guard let user = loggedInUser else {
            return false
        }
        
        return user.role == .MANAGER || user.role == .ADMIN
    }

    func user(id: Int) async throws -> UserResponse {
        if let cached = users[id] {
            return cached
        }

        loadState = .loading
        defer { loadState = .idle }
        
        do {
            let user = try await userService.getUserById(id: id)
            
            users[id] = user
            
            return user
        } catch {
            errorView.show(error)
            throw error
        }
    }

    private func loadLoggedInUser(id: Int) async {
        loadState = .loading
        defer { loadState = .idle }
        
        do {
            let user = try await self.user(id: id)

            self.loggedInUser = user
        } catch {
            errorView.show(error)
        }
    }
    
    func changePassword(
        id: Int64,
        username: String,
        currentPassword: String,
        newPassword: String
    ) async -> Bool {

        loadState = .loading
        defer { loadState = .idle }

        do {
            try await userService.changePassword(
                id: id,
                currentPassword: currentPassword,
                newPassword: newPassword
            )

            await eventBus.publish(
                .user(
                    .passwordChanged(
                        username: username,
                        newPassword: newPassword
                    )
                )
            )
            
            return true

        } catch {
            errorView.show(error)
        }
        
        return false
    }

    func clear() {
        users.removeAll()
        loggedInUser = nil
        loadState = .idle
    }

    private func startListening() {
        eventTask = Task { [weak self] in
            guard let self else { return }

            let events = await eventBus.subscribe()

            for await event in events {
                switch event {

                case .auth(.loggedIn(let id)):
                    await self.loadLoggedInUser(id: id)

                case .auth(.loggedOut):
                    self.clear()
                    
                default:
                    break
                }
            }
        }
    }
}
