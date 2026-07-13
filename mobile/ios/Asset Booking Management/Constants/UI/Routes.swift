//
//  Routes.swift
//  Asset Booking Management
//
//  Created by Jakov  Petric on 10.07.2026..
//

import Foundation

enum AssetRoute: Hashable {
    case detail(Int64)
    case booking(Int64)
    case success(BookingConfirmation)
}

struct BookingConfirmation: Hashable {
    let assetId: Int64
    let fromDate: Date
    let toDate: Date
}
