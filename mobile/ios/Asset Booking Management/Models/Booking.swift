//
//  Booking.swift
//  Asset Booking Management
//
//  Created by Jakov  Petric on 18.06.2026..
//

import Foundation

enum AvailabilityStatus {
    case dayBooked
    case hourBooked
}

enum BookingMode {
    case recurring
    case hourly
    case daily
}

enum BookingStatus: String, Codable, CaseIterable, Identifiable {
    case APPROVED
    case PENDING
    case CANCELLED
    case REJECTED
    case COMPLETED
    
    var id: Self { self }
}

enum BookingPeriod: String, Codable, CaseIterable, Identifiable {
    case HOUR
    case DAY
    case WEEK
    case MONTH
    
    var id: Self { self }
}

struct BookingUserSummary: Codable, Identifiable {
    let id: Int64
    let name: String
    let surname: String
    let email: String
    let role: Role
    let managerEmail: String
    
    var full_name: String {
        return "\(self.name) \(self.surname)"
    }
}

extension BookingUserSummary {
    static var preview: BookingUserSummary {
        return BookingUserSummary(id: 1, name: "Jakov", surname: "Test", email: "test@test.com", role: .ADMIN, managerEmail: "test@manager.com")
    }
}

struct BookingCategorySummary: Codable, Identifiable {
    let id: Int64
    let name: String
    let bookingPeriod: BookingPeriod
    let approval: Bool
}

struct BookingAssetSummary: Codable, Identifiable {
    let id: Int64
    let name: String
    let category: BookingCategorySummary
    let status: AssetStatus
    let description: String
    let location: String
}

struct BookingResponse: Codable, Identifiable {
    let id: Int64
    let user: BookingUserSummary
    let asset: BookingAssetSummary
    let status: BookingStatus
    let bookingStart: Date
    let bookingEnd: Date
    let notes: String?
}

struct BookingListResponse: Codable {
    let content: [BookingResponse]
    let last: Bool
    let number: Int
    let totalPages: Int
}

struct BookingStatusUpdateRequest: Codable {
    let status: BookingStatus
}

extension BookingResponse {
    static let mockData = mockBookingData
}

// REQUESTS

struct TimeSlotRequest: Codable {
    let bookingStart: String
    let bookingEnd: String
}

struct RecurringBookingCreateRequest: Codable {
    let userId: Int64
    let assetId: Int64
    let timeSlots: [TimeSlotRequest]
    let notes: String
}

struct BookingCreateRequest: Codable {
    let userId: Int64
    let assetId: Int64
    let bookingStart: String
    let bookingEnd: String
    let notes: String
}
