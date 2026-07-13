//
//  Constants.swift
//  Asset Booking Management
//
//  Created by Jakov  Petric on 17.06.2026..
//

import Foundation

enum TabItem: CaseIterable, Identifiable {
    case home, asset, booking, profile

    var id: Self { self }

    var title: String {
        switch self {
            case .home: "Home"
            case .asset: "Assets"
            case .booking: "Bookings"
            case .profile: "Profile"
        }
    }

    var icon: String {
        switch self {
            case .home: "house"
            case .asset: "laptopcomputer"
            case .booking: "calendar"
            case .profile: "person"
        }
    }
}
