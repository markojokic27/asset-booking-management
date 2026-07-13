//
//  AppContainer.swift
//  Asset Booking Management
//
//  Created by Jakov  Petric on 30.06.2026..
//

import Foundation

struct AppContainer {
    let apiClient: APIClient
    
    let authService: AuthService
    let authRepository: AuthRepository
    
    let assetService: AssetService
    let assetRepository: AssetRepository
    
    let bookingService: BookingService
    let bookingRepository: BookingRepository
    
    let userService: UserService
    let userRepository: UserRepository
    
    let departmentService: DepartmentService
    let departmentRepository: DepartmentRepository
    
    let assetCategoryService: AssetCategoryService
    let assetCategoryRepository: AssetCategoryRepository
    
    let eventBus: EventBus
    let errorView: ErrorView
    let toastManager: ToastManager
    
    init() {
        let config = APIConfig.dev
        // let tokenStore = InMemoryTokenStore()
        let tokenStore = KeychainTokenStore()
        
        errorView = ErrorView()
        toastManager = ToastManager()
        
        apiClient = URLSessionAPIClient(config: config, tokenStore: tokenStore)
        eventBus = EventBus()
        
        authService = AuthService(api: apiClient, tokenStore: tokenStore)
        authRepository = AuthRepository(authService: authService, eventBus: eventBus, errorView: errorView)
        
        assetService = AssetService(api: apiClient)
        assetRepository = AssetRepository(assetService: assetService, eventBus: eventBus, errorView: errorView)
        
        bookingService = BookingService(api: apiClient)
        bookingRepository = BookingRepository(bookingService: bookingService, eventBus: eventBus, errorView: errorView)
        
        userService = UserService(api: apiClient)
        userRepository = UserRepository(userService: userService, eventBus: eventBus, errorView: errorView)
        
        departmentService = DepartmentService(api: apiClient)
        departmentRepository = DepartmentRepository(departmentService: departmentService, eventBus: eventBus, errorView: errorView)
        
        assetCategoryService = AssetCategoryService(api: apiClient)
        assetCategoryRepository = AssetCategoryRepository(assetCategoryService: assetCategoryService, eventBus: eventBus, errorView: errorView)
    }
}
