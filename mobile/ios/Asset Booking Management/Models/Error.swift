//
//  Error.swift
//  Asset Booking Management
//
//  Created by Jakov  Petric on 07.07.2026..
//

import Foundation

enum AuthError: LocalizedError {
    case emptyUsername
    case emptyPassword
    case unauthorized
    case sessionExpired
    
    var errorDescription: String? {
        switch self {
        case .emptyPassword:
            return "Password cannot be empty."
        case .emptyUsername:
            return "Username cannot be empty"
        case .unauthorized:
            return "You have no authorization for this action"
        case .sessionExpired:
            return "Tokens expired. Please log in again."
        }
    }
}

enum AssetError: LocalizedError {
    case notFound
    
    var errorDescription: String? {
        switch self {
        case .notFound:
            return "Asset not found"
        }
    }
}

enum AssetCategoryError: LocalizedError {
    case notFound
    
    var errorDescription: String? {
        switch self {
        case .notFound:
            return "Asset not found"
        }
    }
}

enum AssetScannerError: LocalizedError {
    case invalidData
    case notFound
    
    var errorDescription: String? {
        switch self {
        case .invalidData:
            return "QR Code data is invalid. Please scan a proper code"
        case .notFound:
            return "Asset not found"
        }
    }
}

enum BookingError: LocalizedError {
    case noTimeSlots
    case weekdayRequired
    
    var errorDescription: String? {
        switch self {
        case .noTimeSlots:
            return "There are no future occurrences in the selected month."
        case .weekdayRequired:
            return "Select at least one recurring day."
        }
    }
}

enum UserError: LocalizedError {
    case invalidCredentials
    
    var errorDescription: String? {
        switch self {
        case .invalidCredentials:
                return "Invalid username or password. Try again."
        }
    }
}

enum APIError: LocalizedError {
    case invalidURL
    case invalidResponse
    case server(statusCode: Int)
    case decoding(Error)
    case encoding(Error)
    case network(Error)

    var errorDescription: String? {
        switch self {
        case .invalidURL:
            return "Invalid URL."

        case .invalidResponse:
            return "Invalid server response."

        case let .server(code):
            return "Server returned status code \(code)."

        case .decoding:
            return "Failed to decode the server response."

        case .encoding:
            return "Failed to encode the request."

        case .network(let error):
            return error.localizedDescription
        }
    }
}
