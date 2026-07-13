//
//  Department.swift
//  Asset Booking Management
//
//  Created by Jakov  Petric on 18.06.2026..
//

import Foundation

enum Department: String, Codable, CaseIterable, Identifiable {
    case SECURE_SERVICES
    case ADVANCE_TECHNOLOGY
    case ARCHITECTURE
    case FINANCE_AND_BUSINESS_ADMINISTRATION
    case MOBILE_AND_SECURITY
    case SYSTEM_TEST
    case HUMAN_RESOURCES
    case CLOUD_AND_DATA_MANAGEMENT
    case DEVOPS
        
    var id: Self { self }
}

struct DepartmentResponse: Codable, Identifiable {
    let id: Int64
    let name: Department
    let managerId: Int64?
}

struct DepartmentListResponse: Codable {
    let content: [DepartmentResponse]
    let last: Bool
    let number: Int
    let totalPages: Int
}
