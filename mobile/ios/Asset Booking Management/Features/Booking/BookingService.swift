//
//  BookingService.swift
//  Asset Booking Management
//
//  Created by Jakov  Petric on 30.06.2026..
//

import Foundation

protocol BookingServiceProtocol {
    func getBookings(
        bookingStart: Date,
        bookingEnd: Date
    ) async throws -> [BookingResponse]
    
    func getAssetBookings(
        assetId: Int64,
        status: BookingStatus,
        bookingStart: Date
    ) async throws -> [BookingResponse]

    func getBookingById(id: Int) async throws -> BookingResponse

    func createBooking(
        request: BookingCreateRequest
    ) async throws -> BookingResponse

    func createRecurringBooking(
        request: RecurringBookingCreateRequest
    ) async throws -> [BookingResponse]

    func approve(id: Int64) async throws -> BookingResponse
    func reject(id: Int64) async throws -> BookingResponse

    func updateBooking(
        id: Int64,
        status: BookingStatus
    ) async throws -> BookingResponse
}

final class BookingService: BookingServiceProtocol {
    
    private let api: APIClient
    
    init(api: APIClient) {
        self.api = api
    }
    
    func getAssetBookings(
        assetId: Int64,
        status: BookingStatus,
        bookingStart: Date
    ) async throws -> [BookingResponse] {
        let pageSize = 150
        var page = 0
        var bookings: [BookingResponse] = []

        while true {
            let response: BookingListResponse = try await api.request(
                BookingEndpoint.getAssetBookings(
                    page: page,
                    size: pageSize,
                    assetId: assetId,
                    status: status.rawValue,
                    bookingStart: bookingStart
                )
            )

            bookings.append(contentsOf: response.content)

            if response.last {
                break
            }

            page += 1
        }

        return bookings
    }
    
    func getBookings(
        bookingStart: Date,
        bookingEnd: Date
    ) async throws -> [BookingResponse] {
        let pageSize = 150
        var page = 1
        var bookings: [BookingResponse] = []

        while true {
            let response: BookingListResponse = try await api.request(
                BookingEndpoint.getBookings(
                    page: page,
                    size: pageSize,
                    bookingStart: bookingStart,
                    bookingEnd: bookingEnd
                )
            )

            bookings.append(contentsOf: response.content)

            if response.last {
                break
            }

            page += 1
        }

        return bookings
    }

    func getBookingById(id: Int) async throws -> BookingResponse {
        try await api.request(BookingEndpoint.getBookingById(id: id))
    }
    
    func approve(id: Int64) async throws -> BookingResponse {
        try await api.request(BookingEndpoint.approveBooking(id: id))
    }
    
    func reject(id: Int64) async throws -> BookingResponse {
        try await api.request(BookingEndpoint.rejectBooking(id: id))
    }
    
    func updateBooking(
        id: Int64,
        status: BookingStatus
    ) async throws -> BookingResponse {
        try await api.request(
            BookingEndpoint.updateBooking(
                id: id,
                status: status
            )
        )
    }
    
    func createBooking(
        request: BookingCreateRequest
    ) async throws -> BookingResponse {
        try await api.request(
            BookingEndpoint.createBooking(request: request)
        )
    }

    func createRecurringBooking(
        request: RecurringBookingCreateRequest
    ) async throws -> [BookingResponse] {
        try await api.request(
            BookingEndpoint.createRecurringBooking(request: request)
        )
    }
}
