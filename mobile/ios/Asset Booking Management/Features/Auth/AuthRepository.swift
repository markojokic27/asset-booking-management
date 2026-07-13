//
//  AuthManager.swift
//  Asset Booking Management
//
//  Created by Jakov  Petric on 17.06.2026..
//

import Foundation
import Observation

@MainActor
@Observable
final class AuthRepository {
    private let authService: AuthServiceProtocol
    private let eventBus: EventBus
    private let errorView: ErrorView

    var authState: AuthState = .unauthenticated
    private(set) var loadState: LoadState = .idle
    private var initialized = false
    
    private var eventTask: Task<Void, Never>?

    init(authService: AuthServiceProtocol, eventBus: EventBus, errorView: ErrorView) {
        self.authService = authService
        self.eventBus = eventBus
        self.errorView = errorView
    
        startListening()
    }

    func initialize() async {
        guard !initialized else { return }
        initialized = true
        
        loadState = .loading
        defer {
            loadState = .idle
        }
        
        if await authService.restoreSession(),
           let userId = authService.currentUserId {
            authState = .authenticated
            await eventBus.publish(.auth(.loggedIn(userId: userId)))
        } else {
            authState = .unauthenticated
            await authService.logout()
            await eventBus.publish(.auth(.loggedOut))
        }
    }

    func login(username: String, password: String) async -> Bool {
        guard !username.isEmpty else {
            errorView.show(AuthError.emptyUsername)
            return false
        }

        guard !password.isEmpty else {
            errorView.show(AuthError.emptyPassword)
            return false
        }

        loadState = .loading
        defer { loadState = .idle }

        do {
            try await authService.login(
                username: username,
                password: password
            )

            guard let userId = authService.currentUserId else {
                return false
            }

            authState = .authenticated
            await eventBus.publish(.auth(.loggedIn(userId: userId)))
            
            return true

        } catch {
            authState = .unauthenticated
            if case APIError.server(let statusCode) = error,
               statusCode == 401 {
                errorView.show(UserError.invalidCredentials)
            } else {
                errorView.show(error)
            }
        }
        
        return false
    }

    func logout() async -> Bool {
        loadState = .loading
        defer { loadState = .idle }
        
        await authService.logout()
        authState = .unauthenticated
        await eventBus.publish(.auth(.loggedOut))
        
        return true
    }
    
    private func startListening() {
        eventTask = Task { [weak self] in
            guard let self else { return }

            let events = await eventBus.subscribe()

            for await event in events {
                switch event {

                case .user(.passwordChanged(let username, let password)):
                    _ = await self.login(
                        username: username,
                        password: password
                    )
                default:
                    break
                    
                }
                
            }
        }
    }
}
