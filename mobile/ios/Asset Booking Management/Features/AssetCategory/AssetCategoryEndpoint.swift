//
//  AssetCategoryEndpoint.swift
//  Asset Booking Management
//
//  Created by Jakov  Petric on 08.07.2026..
//

import Foundation

enum AssetCategoryEndpoint: Endpoint {
    case getAssetCategories(page: Int, size: Int)
    
    var path: String {
        switch self {
        case .getAssetCategories:
            return "/asset-categories"
        }
    }
    
    var method: HTTPMethod {
        switch self {
        case .getAssetCategories:
                .get
        }
    }
    
    var queryItems: [URLQueryItem]? {
        switch self {
        case let .getAssetCategories(page, size):
            return [
                URLQueryItem(name: "page", value: String(page)),
                URLQueryItem(name: "size", value: String(size))
            ]
        }
    }
}
