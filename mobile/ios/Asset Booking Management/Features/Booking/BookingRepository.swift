//
//  BookingRepository.swift
//  Asset Booking Management
//
//  Created by Jakov  Petric on 30.06.2026..
//

import Foundation

@Observable
final class BookingRepository {

    private let bookingService: BookingServiceProtocol
    private let eventBus: EventBus
    private let errorView: ErrorView

    private(set) var bookings: [Int64: BookingResponse] = [:]
    private(set) var loadState: LoadState = .idle
    
    var bookingStart = Calendar.current.date(byAdding: .month, value: -1, to: .now)!
    var bookingEnd = Calendar.current.date(byAdding: .month, value: 1, to: .now)!
    
    private var eventTask: Task<Void, Never>?

    init(
        bookingService: BookingServiceProtocol,
        eventBus: EventBus,
        errorView: ErrorView
    ) {
        self.bookingService = bookingService
        self.eventBus = eventBus
        self.errorView = errorView
        
        startListening()
    }

    var bookingList: [BookingResponse] {
        Array(bookings.values)
    }
    
    func userBookingCount(id: Int64) -> Int {
        bookings.values.filter { $0.user.id == id }.count
    }
    
    func filterBookingsByStatus(status: BookingStatus) -> [BookingResponse] {
        bookings.values.filter { $0.status == status }
    }

    func loadIfNeeded() async {
        guard loadState == .idle else { return }
        await loadBookings(bookingStart: bookingStart, bookingEnd: bookingStart)
    }

    func loadBookings(bookingStart: Date, bookingEnd: Date) async {
        loadState = .loading
        defer { loadState = .idle }
        
        do {
            let result = try await bookingService.getBookings(
                bookingStart: bookingStart,
                bookingEnd: bookingEnd
            )

            bookings = Dictionary(
                uniqueKeysWithValues: result.map { ($0.id, $0) }
            )

        } catch {
            errorView.show(error)
        }
    }
    
    func loadAssetBookings(assetId: Int64, status: BookingStatus, bookingStart: Date) async -> [BookingResponse] {
        loadState = .loading
        defer { loadState = .idle }
        
        do {
            let result = try await bookingService.getAssetBookings(
                assetId: assetId,
                status: status,
                bookingStart: bookingStart
            )

            return result
        } catch {
            errorView.show(error)
        }
        
        return []
    }

    func refresh() async {
        await loadBookings(bookingStart: bookingStart, bookingEnd: bookingStart)
    }
    
    func booking(id: Int64) -> BookingResponse? {
        bookings[id]
    }

    func clear() {
        bookings = [:]
        loadState = .idle
    }
    
    func updateBooking(id: Int64, status: BookingStatus) async {
        do {
            let updated = try await bookingService.updateBooking(
                id: id,
                status: status
            )

            bookings[updated.id] = updated
        } catch {
            errorView.show(error)
        }
    }
    
    func approveBooking(id: Int64) async {
        do {
            let updated = try await bookingService.approve(id: id)

            bookings[updated.id] = updated
        } catch {
            errorView.show(error)
        }
    }
    
    func rejectBooking(id: Int64) async {
        do {
            let updated = try await bookingService.reject(id: id)

            bookings[updated.id] = updated
        } catch {
            errorView.show(error)
        }
    }
    
    func createBooking(
        userId: Int64,
        assetId: Int64,
        bookingStart: Date,
        bookingEnd: Date,
        notes: String
    ) async -> Bool {
        loadState = .loading
        defer { loadState = .idle }
        
        let request = BookingCreateRequest(
            userId: userId, assetId: assetId, bookingStart: bookingStart.ISO8601Format(), bookingEnd: bookingEnd.ISO8601Format(), notes: notes
        )
        
        print(request)
        
        do {
            let created = try await bookingService.createBooking(
                request: request
            )

            bookings[created.id] = created
            return true
        } catch {
            errorView.show(error)
            return false
        }
    }

    func createRecurringBooking(
        userId: Int64,
        assetId: Int64,
        timeSlots: [TimeSlotRequest],
        selectedWeekdays: Set<Int>,
        notes: String
    ) async -> Bool {
        
        guard !selectedWeekdays.isEmpty else {
            errorView.show(BookingError.weekdayRequired)
            return false
        }

        guard !timeSlots.isEmpty else {
            errorView.show(BookingError.noTimeSlots)
            return false
        }
        
        loadState = .loading
        defer { loadState = .idle }
        
        let request = RecurringBookingCreateRequest(
            userId: userId,
            assetId: assetId,
            timeSlots: timeSlots,
            notes: notes
        )

        do {
            let created = try await bookingService.createRecurringBooking(
                request: request
            )

            for booking in created {
                bookings[booking.id] = booking
            }

            return true
        } catch {
            errorView.show(error)
            return false
        }
    }
    
    private func startListening() {
        eventTask = Task { [weak self] in
            guard let self else { return }

            let events = await eventBus.subscribe()

            for await event in events {
                switch event {

                case .auth(.loggedIn(_)):
                    await self.loadBookings(
                        bookingStart: bookingStart,
                        bookingEnd: bookingEnd
                    )

                case .auth(.loggedOut):
                    self.clear()
                    
                default:
                    break
                }
            }
        }
    }
}
