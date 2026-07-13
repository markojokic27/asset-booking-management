//
//  AssetCategory.swift
//  Asset Booking Management
//
//  Created by Jakov  Petric on 18.06.2026..
//

import Foundation

enum AssetCategoryBookingPeriod: String, Codable, CaseIterable, Identifiable {
    case HOUR
    case DAY
    case WEEK
    case MONTH
        
    var id: Self { self }
}

struct AssetCategoryResponse: Codable, Identifiable {
    let id: Int64
    let name: String
    let description: String
    let bookingPeriod: AssetCategoryBookingPeriod
    let approval: Bool
}

struct AssetCategoryListResponse: Codable {
    let content: [AssetCategoryResponse]
    let last: Bool
    let number: Int
    let totalPages: Int
}

extension AssetCategoryResponse {
    var bookingMode: BookingMode {
        if name.caseInsensitiveCompare("Parking") == .orderedSame {
            return .recurring
        }

        if bookingPeriod == .HOUR {
            return .hourly
        }

        return .daily
    }
}
