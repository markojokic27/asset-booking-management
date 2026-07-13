//
//  AssetService.swift
//  Asset Booking Management
//
//  Created by Jakov  Petric on 30.06.2026..
//

import Foundation

protocol AssetServiceProtocol {
    func getAssets() async throws -> [AssetResponse]
    func getAssetById(id: Int) async throws -> AssetResponse
}

final class AssetService: AssetServiceProtocol {
    
    private let api: APIClient
    
    init(api: APIClient) {
        self.api = api
    }
    
    func getAssets() async throws -> [AssetResponse] {
        let pageSize = 150
        var page = 1
        var assets: [AssetResponse] = []

        while true {
            let response: AssetListResponse = try await api.request(
                AssetEndpoint.getAssets(page: page, size: pageSize)
            )

            assets.append(contentsOf: response.content)

            if response.last {
                break
            }

            page += 1
        }

        return assets
    }

    func getAssetById(id: Int) async throws -> AssetResponse {
        try await api.request(AssetEndpoint.getAssetById(id: id))
    }
}
