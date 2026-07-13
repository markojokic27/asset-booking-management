//
//  DepartmentRepository.swift
//  Asset Booking Management
//
//  Created by Jakov  Petric on 08.07.2026..
//

import Foundation
import Observation

@MainActor
@Observable
final class DepartmentRepository {

    private let departmentService: DepartmentServiceProtocol
    private let eventBus: EventBus
    private let errorView: ErrorView

    private(set) var departments: [Int64: DepartmentResponse] = [:]

    private(set) var loadState: LoadState = .idle

    private var eventTask: Task<Void, Never>?

    init(departmentService: DepartmentServiceProtocol, eventBus: EventBus, errorView: ErrorView) {
        self.departmentService = departmentService
        self.eventBus = eventBus
        self.errorView = errorView

        startListening()
    }
    
    func department(id: Int64) -> DepartmentResponse? {
        departments[id]
    }

    func loadIfNeeded() async {
        guard loadState == .idle else { return }
        await loadDepartments()
    }

    func loadDepartments() async {
        loadState = .loading
        defer { loadState = .idle }

        do {
            let result = try await departmentService.getDepartments()

            departments = Dictionary(
                uniqueKeysWithValues: result.map { ($0.id, $0) }
            )

        } catch {
            errorView.show(error)
        }
    }

    func refresh() async {
        await loadDepartments()
    }

    func clear() {
        departments.removeAll()
        loadState = .idle
    }

    private func startListening() {
        eventTask = Task { [weak self] in
            guard let self else { return }

            let events = await eventBus.subscribe()

            for await event in events {
                switch event {

                case .auth(.loggedIn(_)):
                    await self.loadDepartments()

                case .auth(.loggedOut):
                    self.clear()
                    
                default:
                    break
                }
            }
        }
    }
}
