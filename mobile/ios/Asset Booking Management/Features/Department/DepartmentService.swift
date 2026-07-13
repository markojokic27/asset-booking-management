//
//  DepartmentService.swift
//  Asset Booking Management
//
//  Created by Jakov  Petric on 08.07.2026..
//

import Foundation

protocol DepartmentServiceProtocol {
    func getDepartments() async throws -> [DepartmentResponse]
}

final class DepartmentService: DepartmentServiceProtocol {
    private let api: APIClient
    
    init(api: APIClient) {
        self.api = api
    }
    
    func getDepartments() async throws -> [DepartmentResponse] {
        let pageSize = 150
        var page = 1
        var departments: [DepartmentResponse] = []

        while true {
            let response: DepartmentListResponse = try await api.request(
                DepartmentEndpoint.getDepartments(page: page, size: pageSize)
            )

            departments.append(contentsOf: response.content)

            if response.last {
                break
            }

            page += 1
        }

        return departments
    }
}
