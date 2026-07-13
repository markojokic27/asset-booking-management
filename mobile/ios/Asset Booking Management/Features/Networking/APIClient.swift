//
//  APIClient.swift
//  Asset Booking Management
//
//  Created by Jakov  Petric on 30.06.2026..
//

import Foundation

protocol APIClient {
    func request<E: Endpoint, T: Decodable>(
        _ endpoint: E
    ) async throws -> T

    func request<E: Endpoint>(
        _ endpoint: E
    ) async throws
}

final class URLSessionAPIClient: APIClient {

    private let config: APIConfig
    private let session: URLSession
    private let tokenStore: TokenStore

    init(
        config: APIConfig,
        session: URLSession = .shared,
        tokenStore: TokenStore
    ) {
        self.config = config
        self.session = session
        self.tokenStore = tokenStore
    }

    func request<E: Endpoint, T: Decodable>(_ endpoint: E) async throws -> T {

        guard var components = URLComponents(
            url: config.baseURL.appending(path: endpoint.path),
            resolvingAgainstBaseURL: false
        ) else {
            throw APIError.invalidResponse
        }

        components.queryItems = endpoint.queryItems

        guard let url = components.url else {
            throw APIError.invalidResponse
        }

        var request = URLRequest(url: url)
        request.httpMethod = endpoint.method.rawValue
        request.httpBody = endpoint.body
        request.allHTTPHeaderFields = endpoint.headers

        if let token = tokenStore.accessToken() {
            request.setValue(
                "Bearer \(token)",
                forHTTPHeaderField: "Authorization"
            )
        }

        do {
            let (data, response) = try await session.data(for: request)

            guard let http = response as? HTTPURLResponse else {
                throw APIError.invalidResponse
            }

            guard (200..<300).contains(http.statusCode) else {
                throw APIError.server(statusCode: http.statusCode)
            }

            do {
                let decoder = JSONDecoder()
                decoder.dateDecodingStrategy = .iso8601

                return try decoder.decode(T.self, from: data)
            } catch {
                throw APIError.decoding(error)
            }

        } catch {
            if let apiError = error as? APIError {
                throw apiError
            }

            throw APIError.network(error)
        }
    }
    
    // Special Void case for 204 No Content
    func request<E: Endpoint>(_ endpoint: E) async throws {
        guard var components = URLComponents(
            url: config.baseURL.appending(path: endpoint.path),
            resolvingAgainstBaseURL: false
        ) else {
            throw APIError.invalidResponse
        }

        components.queryItems = endpoint.queryItems

        guard let url = components.url else {
            throw APIError.invalidResponse
        }

        var request = URLRequest(url: url)
        request.httpMethod = endpoint.method.rawValue
        request.httpBody = endpoint.body
        request.allHTTPHeaderFields = endpoint.headers

        if let token = tokenStore.accessToken() {
            request.setValue("Bearer \(token)", forHTTPHeaderField: "Authorization")
        }

        let (_, response) = try await session.data(for: request)

        guard let http = response as? HTTPURLResponse else {
            throw APIError.invalidResponse
        }

        guard (200..<300).contains(http.statusCode) else {
            throw APIError.server(statusCode: http.statusCode)
        }
    }
}
