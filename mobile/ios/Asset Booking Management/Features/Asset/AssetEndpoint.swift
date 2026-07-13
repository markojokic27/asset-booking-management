//
//  AssetEndpoint.swift
//  Asset Booking Management
//
//  Created by Jakov  Petric on 30.06.2026..
//

import Foundation

enum AssetEndpoint: Endpoint {
    case getAssets(page: Int, size: Int)
    case getAssetById(id: Int)

    var path: String {
        switch self {
        case .getAssets:
            return "/assets"
        case let .getAssetById(id):
            return "/assets/\(id)"
        }
    }

    var method: HTTPMethod {
        .get
    }

    var queryItems: [URLQueryItem]? {
        switch self {
        case let .getAssets(page, size):
            return [
                URLQueryItem(name: "page", value: String(page)),
                URLQueryItem(name: "size", value: String(size))
            ]

        case .getAssetById:
            return nil
        }
    }
}
