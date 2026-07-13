//
//  AssetViewModel.swift
//  Asset Booking Management
//
//  Created by Jakov  Petric on 29.06.2026..
//

import Foundation

@Observable
final class AssetRepository {

    private let assetService: AssetServiceProtocol
    private let eventBus: EventBus
    private let errorView: ErrorView

    private(set) var assets: [Int64: AssetResponse] = [:]
    private(set) var loadState: LoadState = .idle

    private var eventTask: Task<Void, Never>?

    init(
        assetService: AssetServiceProtocol,
        eventBus: EventBus,
        errorView: ErrorView
    ) {
        self.assetService = assetService
        self.eventBus = eventBus
        self.errorView = errorView

        startListening()
    }

    var assetList: [AssetResponse] {
        Array(assets.values)
    }

    func loadIfNeeded() async {
        guard loadState == .idle else { return }
        await loadAssets()
    }

    func loadAssets() async {
        loadState = .loading
        defer { loadState = .idle }

        do {
            let result = try await assetService.getAssets()

            assets = Dictionary(
                uniqueKeysWithValues: result.map { ($0.id, $0) }
            )

        } catch {
            errorView.show(error)
        }
    }

    func refresh() async {
        await loadAssets()
    }

    func asset(id: Int64) -> AssetResponse? {
        assets[id]
    }

    func clear() {
        assets = [:]
        loadState = .idle
    }

    private func startListening() {
        eventTask = Task { [weak self] in
            guard let self else { return }

            let events = await eventBus.subscribe()

            for await event in events {
                switch event {
                case .auth(.loggedIn(_)):
                    await self.loadAssets()

                case .auth(.loggedOut):
                    self.clear()

                default:
                    break
                }
            }
        }
    }
}
