//
//  EventBus.swift
//  Asset Booking Management
//
//  Created by Jakov  Petric on 06.07.2026..
//

import Foundation

actor EventBus {

    private var continuations: [UUID: AsyncStream<AppEvent>.Continuation] = [:]
    // private var buffer: [AppEvent] = []

    func subscribe() -> AsyncStream<AppEvent> {
        let id = UUID()

        return AsyncStream { continuation in
            continuations[id] = continuation

            // for event in buffer {
            //    continuation.yield(event)
            // }

            continuation.onTermination = { [id] _ in
                Task { await self.unsubscribe(id) }
            }
        }
    }

    func publish(_ event: AppEvent) {
        // buffer.append(event)

        for continuation in continuations.values {
            continuation.yield(event)
        }
    }

    private func unsubscribe(_ id: UUID) {
        continuations.removeValue(forKey: id)
    }
}
