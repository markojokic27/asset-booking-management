//
//  TokenStore.swift
//  Asset Booking Management
//
//  Created by Jakov  Petric on 30.06.2026..
//

import Foundation
import Security
import JWTDecode

protocol TokenStore {
    func accessToken() -> String?
    func refreshToken() -> String?

    func save(
        accessToken: String,
        refreshToken: String
    )

    func clear()
}

final class KeychainTokenStore: TokenStore {

    private let service = Bundle.main.bundleIdentifier ?? "app"
    private let account = "refresh-token"

    private var access: String?

    func accessToken() -> String? {
        access
    }

    func refreshToken() -> String? {
        guard let token = readKeychain() else {
            return nil
        }

        if isExpired(token) {
            deleteKeychain()
            return nil
        }

        return token
    }

    func save(accessToken: String, refreshToken: String) {
        self.access = accessToken
        saveKeychain(refreshToken)
    }

    func clear() {
        access = nil
        deleteKeychain()
    }

    private func saveKeychain(_ token: String) {
        let data = Data(token.utf8)

        let query: [String: Any] = [
            kSecClass as String: kSecClassGenericPassword,
            kSecAttrService as String: service,
            kSecAttrAccount as String: account,
            kSecValueData as String: data
        ]

        SecItemDelete(query as CFDictionary)
        SecItemAdd(query as CFDictionary, nil)
    }

    private func readKeychain() -> String? {
        let query: [String: Any] = [
            kSecClass as String: kSecClassGenericPassword,
            kSecAttrService as String: service,
            kSecAttrAccount as String: account,
            kSecReturnData as String: true,
            kSecMatchLimit as String: kSecMatchLimitOne
        ]

        var result: AnyObject?

        let status = SecItemCopyMatching(
            query as CFDictionary,
            &result
        )

        guard status == errSecSuccess,
              let data = result as? Data
        else {
            return nil
        }

        return String(data: data, encoding: .utf8)
    }

    private func deleteKeychain() {
        let query: [String: Any] = [
            kSecClass as String: kSecClassGenericPassword,
            kSecAttrService as String: service,
            kSecAttrAccount as String: account
        ]

        SecItemDelete(query as CFDictionary)
    }

    private func isExpired(_ token: String) -> Bool {
        do {
            let jwt = try decode(jwt: token)

            guard let expiration = jwt.claim(name: "exp").integer else {
                return true
            }

            return Date(timeIntervalSince1970: TimeInterval(expiration)) <= Date()
        } catch {
            return true
        }
    }
}

final class InMemoryTokenStore: TokenStore {
    private var access: String?
    private var refresh: String?
    
    func accessToken() -> String? {
        access
    }
    
    func refreshToken() -> String? {
        refresh
    }

    func save(accessToken: String, refreshToken: String) {
        self.access = accessToken
        self.refresh = refreshToken
    }

    func clear() {
        access = nil
        refresh = nil
    }
}
