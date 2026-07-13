//
//  AuthService.swift
//  Asset Booking Management
//
//  Created by Jakov  Petric on 30.06.2026..
//

import Foundation
import JWTDecode

protocol AuthServiceProtocol {
    var currentUserId: Int? { get }
    var accessToken: String? { get }

    func login(username: String, password: String) async throws
    func logout() async
    func restoreSession() async -> Bool
    func refreshSession() async throws
}

final class AuthService: AuthServiceProtocol {

    private let api: APIClient
    private let tokenStore: TokenStore

    var accessToken: String? {
        tokenStore.accessToken()
    }
    
    var currentUserId: Int? {
        guard let token = accessToken else {
            return nil
        }

        do {
            let jwt = try decode(jwt: token)
            return jwt.claim(name: "userId").integer
        } catch {
            return nil
        }   
    }

    init(
        api: APIClient,
        tokenStore: TokenStore
    ) {
        self.api = api
        self.tokenStore = tokenStore
    }
    
    func login(username: String, password: String) async throws {

        let response: LoginResponse = try await api.request(
            AuthEndpoint.login(username: username, password: password)
        )

        tokenStore.save(
            accessToken: response.accessToken,
            refreshToken: response.refreshToken
        )
    }
    
    func refreshSession() async throws {
        guard let refreshToken = tokenStore.refreshToken() else {
            throw AuthError.sessionExpired
        }

        let response: LoginResponse = try await api.request(
            AuthEndpoint.refresh(refreshToken: refreshToken)
        )

        tokenStore.save(
            accessToken: response.accessToken,
            refreshToken: response.refreshToken
        )
    }

    func restoreSession() async -> Bool {
        guard tokenStore.refreshToken() != nil else {
            return false
        }

        do {
            try await refreshSession()
            return true
        } catch {
            tokenStore.clear()
            return false
        }
    }

    func logout() async {
        tokenStore.clear()
    }
}
