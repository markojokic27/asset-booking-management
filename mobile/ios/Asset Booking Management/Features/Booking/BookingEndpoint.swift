//
//  BookingEndpoint.swift
//  Asset Booking Management
//
//  Created by Jakov  Petric on 30.06.2026..
//

import Foundation

enum BookingEndpoint: Endpoint {
    case getBookings(
        page: Int,
        size: Int,
        bookingStart: Date,
        bookingEnd: Date
    )
    case getAssetBookings(
        page: Int,
        size: Int,
        assetId: Int64,
        status: String,
        bookingStart: Date
    )
    case getBookingById(id: Int)
    case createBooking(request: BookingCreateRequest)
    case createRecurringBooking(request: RecurringBookingCreateRequest)
    case approveBooking(id: Int64)
    case rejectBooking(id: Int64)
    case updateBooking(id: Int64, status: BookingStatus)

    var path: String {
        switch self {
        case .getBookings, .getAssetBookings:
            "/bookings"

        case let .getBookingById(id):
            "/bookings/\(id)"

        case .createBooking:
            "/bookings"

        case .createRecurringBooking:
            "/bookings/recurring"

        case let .approveBooking(id):
            "/bookings/\(id)/approve"

        case let .rejectBooking(id):
            "/bookings/\(id)/reject"

        case let .updateBooking(id, _):
            "/bookings/\(id)"
        }
    }

    var method: HTTPMethod {
        switch self {
        case .getBookings, .getBookingById, .getAssetBookings:
            .get

        case .createBooking, .createRecurringBooking, .approveBooking, .rejectBooking:
            .post

        case .updateBooking:
            .patch
        }
    }

    var queryItems: [URLQueryItem]? {
        switch self {
        case let .getBookings(page, size, bookingStart, bookingEnd):
            return [
                URLQueryItem(name: "page", value: "\(page)"),
                URLQueryItem(name: "size", value: "\(size)"),
                URLQueryItem(
                    name: "bookingStart",
                    value: DateFormatterProvider.iso8601.string(from: bookingStart)
                ),
                URLQueryItem(
                    name: "bookingEnd",
                    value: DateFormatterProvider.iso8601.string(from: bookingEnd)
                )
            ]
        case let .getAssetBookings(page, size, assetId, status, bookingStart):
            return [
                URLQueryItem(name: "page", value: "\(page)"),
                URLQueryItem(name: "size", value: "\(size)"),
                URLQueryItem(name: "assetId", value: "\(assetId)"),
                URLQueryItem(name: "status", value: "\(status)"),
                URLQueryItem(
                    name: "bookingStart",
                    value: DateFormatterProvider.iso8601.string(from: bookingStart)
                )
            ]

        default:
            return nil
        }
    }

    var body: Data? {
        switch self {
        case let .createBooking(request):
            return try? JSONEncoder().encode(request)

        case let .createRecurringBooking(request):
            return try? JSONEncoder().encode(request)

        case let .updateBooking(_, status):
            return try? JSONEncoder().encode(
                BookingStatusUpdateRequest(status: status)
            )

        default:
            return nil
        }
    }
}
