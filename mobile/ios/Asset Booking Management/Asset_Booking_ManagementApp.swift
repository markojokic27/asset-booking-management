//
//  Asset_Booking_ManagementApp.swift
//  Asset Booking Management
//
//  Created by Jakov  Petric on 17.06.2026..
//

import SwiftUI

@main
struct Asset_Booking_ManagementApp: App {
    private let container = AppContainer()
    
    var body: some Scene {
        WindowGroup {
            ContentView()
                .task {
                    await container.authRepository.initialize()
                }
                .environment(container.authRepository)
                .environment(container.assetRepository)
                .environment(container.bookingRepository)
                .environment(container.userRepository)
                .environment(container.departmentRepository)
                .environment(container.assetCategoryRepository)
                .environment(container.errorView)
                .environment(container.toastManager)
        }
    }
}
