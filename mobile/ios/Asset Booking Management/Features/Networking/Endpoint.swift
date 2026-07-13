//
//  Endpoint.swift
//  Asset Booking Management
//
//  Created by Jakov  Petric on 30.06.2026..
//

import Foundation

// Defines data to be sent to the server with the request
// Path - Defines the url extension
// Method - HTTP Method
// Headers - Self-explanitory
// Body - Data that is sent to the server

protocol Endpoint {
    var path: String { get }
    var method: HTTPMethod { get }
    var headers: [String: String] { get }
    var body: Data? { get }
    var queryItems: [URLQueryItem]? { get }
}

extension Endpoint {

    var headers: [String: String] {
        ["Content-Type": "application/json"]
    }

    var body: Data? { nil }

    var queryItems: [URLQueryItem]? { nil }
}
