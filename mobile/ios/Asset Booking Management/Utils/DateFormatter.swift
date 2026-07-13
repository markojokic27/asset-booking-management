//
//  DateFormatter.swift
//  Asset Booking Management
//
//  Created by Jakov  Petric on 06.07.2026..
//

import Foundation

enum DateFormatterProvider {
    static let iso8601: ISO8601DateFormatter = {
        let formatter = ISO8601DateFormatter()
        formatter.timeZone = TimeZone(secondsFromGMT: 0)
        return formatter
    }()
}
