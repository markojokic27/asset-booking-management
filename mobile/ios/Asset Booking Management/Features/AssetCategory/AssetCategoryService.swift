//
//  AssetCategoryService.swift
//  Asset Booking Management
//
//  Created by Jakov  Petric on 08.07.2026..
//

import Foundation

protocol AssetCategoryServiceProtocol {
    func getAssetCategories() async throws -> [AssetCategoryResponse]
}

final class AssetCategoryService: AssetCategoryServiceProtocol {
    private let api: APIClient
    
    init(api: APIClient) {
        self.api = api
    }
    
    func getAssetCategories() async throws -> [AssetCategoryResponse] {
        let pageSize = 150
        var page = 1
        var categories: [AssetCategoryResponse] = []

        while true {
            let response: AssetCategoryListResponse = try await api.request(
                AssetCategoryEndpoint.getAssetCategories(page: page, size: pageSize)
            )

            categories.append(contentsOf: response.content)

            if response.last {
                break
            }

            page += 1
        }

        return categories
    }
}
