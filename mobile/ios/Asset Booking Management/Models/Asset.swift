//
//  Asset.swift
//  Asset Booking Management
//
//  Created by Jakov  Petric on 18.06.2026..
//

import Foundation

enum AssetStatus: String, Codable, CaseIterable, Identifiable {
    case ACTIVE
    case INACTIVE
    case DAMAGED
    case DELETED
    
    var id: Self { self }
}

struct AssetResponse: Codable, Identifiable {
    let id: Int64
    let name: String
    let categoryId: Int64
    let description: String
    let code: String
    let status: AssetStatus
    let location: String
}

struct AssetListResponse: Codable {
    let content: [AssetResponse]
    let last: Bool
    let number: Int
    let totalPages: Int
}

extension AssetResponse {
    static let mockData = mockAssetData
}
