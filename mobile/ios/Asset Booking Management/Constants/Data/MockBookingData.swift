//
//  Booking.swift
//  Asset Booking Management
//
//  Created by Jakov  Petric on 18.06.2026..
//
import Foundation

private let iso8601 = ISO8601DateFormatter()

let mockBookingData: [BookingResponse] = [
    BookingResponse(
        id: 1,
        user: BookingUserSummary.preview,
        asset: BookingAssetSummary(
            id: 1,
            name: "MacBook Pro 14",
            category: BookingCategorySummary(
                id: 1,
                name: "Laptop",
                bookingPeriod: .WEEK,
                approval: true
            ),
            status: .ACTIVE,
            description: "Development laptop",
            location: "IT Department"
        ),
        status: .APPROVED,
        bookingStart: iso8601.date(from: "2026-06-01T09:00:00Z")!,
        bookingEnd: iso8601.date(from: "2026-06-05T17:00:00Z")!,
        notes: "Needed for project work"
    ),

    BookingResponse(
        id: 2,
        user: BookingUserSummary.preview,
        asset: BookingAssetSummary(
            id: 2,
            name: "iPhone 15",
            category: BookingCategorySummary(
                id: 2,
                name: "Mobile",
                bookingPeriod: .WEEK,
                approval: false
            ),
            status: .ACTIVE,
            description: "Company mobile device",
            location: "Sales"
        ),
        status: .PENDING,
        bookingStart: iso8601.date(from: "2026-06-01T09:00:00Z")!,
        bookingEnd: iso8601.date(from: "2026-06-05T17:00:00Z")!,
        notes: nil
    ),

    BookingResponse(
        id: 3,
        user: BookingUserSummary.preview,
        asset: BookingAssetSummary(
            id: 3,
            name: "Dell Latitude 7420",
            category: BookingCategorySummary(
                id: 1,
                name: "Laptop",
                bookingPeriod: .WEEK,
                approval: true
            ),
            status: .ACTIVE,
            description: "Business laptop",
            location: "Head Office"
        ),
        status: .APPROVED,
        bookingStart: iso8601.date(from: "2026-06-01T09:00:00Z")!,
        bookingEnd: iso8601.date(from: "2026-06-05T17:00:00Z")!,
        notes: "Client visit"
    ),

    BookingResponse(
        id: 4,
        user: BookingUserSummary.preview,
        asset: BookingAssetSummary(
            id: 4,
            name: "iPad Air",
            category: BookingCategorySummary(
                id: 3,
                name: "Tablet",
                bookingPeriod: .WEEK,
                approval: true
            ),
            status: .ACTIVE,
            description: "Field tablet",
            location: "Operations"
        ),
        status: .CANCELLED,
        bookingStart: iso8601.date(from: "2026-06-01T09:00:00Z")!,
        bookingEnd: iso8601.date(from: "2026-06-05T17:00:00Z")!,
        notes: "Cancelled by user"
    ),

    BookingResponse(
        id: 5,
        user: BookingUserSummary.preview,
        asset: BookingAssetSummary(
            id: 5,
            name: "Dell Monitor 32",
            category: BookingCategorySummary(
                id: 4,
                name: "Monitor",
                bookingPeriod: .WEEK,
                approval: false
            ),
            status: .ACTIVE,
            description: "External monitor",
            location: "Engineering"
        ),
        status: .APPROVED,
        bookingStart: iso8601.date(from: "2026-06-01T09:00:00Z")!,
        bookingEnd: iso8601.date(from: "2026-06-05T17:00:00Z")!,
        notes: nil
    ),

    BookingResponse(
        id: 6,
        user: BookingUserSummary.preview,
        asset: BookingAssetSummary(
            id: 6,
            name: "Samsung Galaxy S24",
            category: BookingCategorySummary(
                id: 2,
                name: "Mobile",
                bookingPeriod: .WEEK,
                approval: false
            ),
            status: .ACTIVE,
            description: "Testing phone",
            location: "QA Lab"
        ),
        status: .PENDING,
        bookingStart: iso8601.date(from: "2026-06-01T09:00:00Z")!,
        bookingEnd: iso8601.date(from: "2026-06-05T17:00:00Z")!,
        notes: "Testing release"
    ),

    BookingResponse(
        id: 7,
        user: BookingUserSummary.preview,
        asset: BookingAssetSummary(
            id: 7,
            name: "Cisco Switch",
            category: BookingCategorySummary(
                id: 5,
                name: "Network",
                bookingPeriod: .WEEK,
                approval: true
            ),
            status: .ACTIVE,
            description: "Network equipment",
            location: "Server Room"
        ),
        status: .APPROVED,
        bookingStart: iso8601.date(from: "2026-06-01T09:00:00Z")!,
        bookingEnd: iso8601.date(from: "2026-06-05T17:00:00Z")!,
        notes: "Maintenance window"
    ),

    BookingResponse(
        id: 8,
        user: BookingUserSummary.preview,
        asset: BookingAssetSummary(
            id: 8,
            name: "Surface Laptop",
            category: BookingCategorySummary(
                id: 1,
                name: "Laptop",
                bookingPeriod: .WEEK,
                approval: true
            ),
            status: .ACTIVE,
            description: "Work laptop",
            location: "Design"
        ),
        status: .REJECTED,
        bookingStart: iso8601.date(from: "2026-06-01T09:00:00Z")!,
        bookingEnd: iso8601.date(from: "2026-06-05T17:00:00Z")!,
        notes: "Asset unavailable"
    ),

    BookingResponse(
        id: 9,
        user: BookingUserSummary.preview,
        asset: BookingAssetSummary(
            id: 9,
            name: "Camera Kit",
            category: BookingCategorySummary(
                id: 6,
                name: "Equipment",
                bookingPeriod: .WEEK,
                approval: true
            ),
            status: .ACTIVE,
            description: "Company camera",
            location: "Media Room"
        ),
        status: .COMPLETED,
        bookingStart: iso8601.date(from: "2026-06-01T09:00:00Z")!,
        bookingEnd: iso8601.date(from: "2026-06-05T17:00:00Z")!,
        notes: "Returned"
    ),

    BookingResponse(
        id: 10,
        user: BookingUserSummary.preview,
        asset: BookingAssetSummary(
            id: 10,
            name: "Lenovo ThinkPad X1",
            category: BookingCategorySummary(
                id: 1,
                name: "Laptop",
                bookingPeriod: .WEEK,
                approval: true
            ),
            status: .ACTIVE,
            description: "Engineering laptop",
            location: "Remote"
        ),
        status: .APPROVED,
        bookingStart: iso8601.date(from: "2026-06-01T09:00:00Z")!,
        bookingEnd: iso8601.date(from: "2026-06-05T17:00:00Z")!,
        notes: "Remote work"
    )
]
