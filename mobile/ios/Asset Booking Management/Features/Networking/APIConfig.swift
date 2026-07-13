//
//  APIConfig.swift
//  Asset Booking Management
//
//  Created by Jakov  Petric on 30.06.2026..
//

import Foundation

struct APIConfig {
    let baseURL: URL

    static let prod = APIConfig(
        baseURL: URL(string: "https://prod.com/v1")!
    )

    static let dev = APIConfig(
        baseURL: URL(string: "http://127.0.0.1:8080/v1")!
    )
}
