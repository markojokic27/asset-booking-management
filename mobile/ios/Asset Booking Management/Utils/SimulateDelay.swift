//
//  SimulateDelay.swift
//  Asset Booking Management
//
//  Created by Jakov  Petric on 17.06.2026..
//

import Foundation

func simulateDelay () async {
    try? await Task.sleep(nanoseconds: 2_000_000_000)
}
