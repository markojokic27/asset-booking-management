//
//  AssetCategoryRepository.swift
//  Asset Booking Management
//
//  Created by Jakov  Petric on 08.07.2026..
//

import Foundation
import Observation

@MainActor
@Observable
final class AssetCategoryRepository {

    private let assetCategoryService: AssetCategoryServiceProtocol
    private let eventBus: EventBus
    private let errorView: ErrorView

    private(set) var assetCategories: [Int64: AssetCategoryResponse] = [:]

    private(set) var loadState: LoadState = .idle

    private var eventTask: Task<Void, Never>?

    init(assetCategoryService: AssetCategoryServiceProtocol, eventBus: EventBus, errorView: ErrorView) {
        self.assetCategoryService = assetCategoryService
        self.eventBus = eventBus
        self.errorView = errorView

        startListening()
    }
    
    func assetCategory(id: Int64) -> AssetCategoryResponse? {
        assetCategories[id]
    }

    func loadIfNeeded() async {
        guard loadState == .idle else { return }
        await loadAssetCategories()
    }

    func loadAssetCategories() async {
        loadState = .loading
        defer { loadState = .idle }

        do {
            let result = try await assetCategoryService.getAssetCategories()

            assetCategories = Dictionary(
                uniqueKeysWithValues: result.map { ($0.id, $0) }
            )

        } catch {
            errorView.show(error)
        }
    }

    func refresh() async {
        await loadAssetCategories()
    }

    func clear() {
        assetCategories.removeAll()
        loadState = .idle
    }

    private func startListening() {
        eventTask = Task { [weak self] in
            guard let self else { return }

            let events = await eventBus.subscribe()

            for await event in events {
                switch event {

                case .auth(.loggedIn(_)):
                    await self.loadAssetCategories()

                case .auth(.loggedOut):
                    self.clear()
                    
                default:
                    break
                }
            }
        }
    }
}
