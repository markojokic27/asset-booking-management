//
//  DepartmentEndpoint.swift
//  Asset Booking Management
//
//  Created by Jakov  Petric on 08.07.2026..
//

import Foundation

enum DepartmentEndpoint: Endpoint {
    case getDepartments(page: Int, size: Int)
    
    var path: String {
        switch self {
        case .getDepartments:
            return "/departments"
        }
    }
    
    var method: HTTPMethod {
        switch self {
        case .getDepartments:
                .get
        }
    }
    
    var queryItems: [URLQueryItem]? {
        switch self {
        case let .getDepartments(page, size):
            return [
                URLQueryItem(name: "page", value: String(page)),
                URLQueryItem(name: "size", value: String(size))
            ]
        }
    }
}
