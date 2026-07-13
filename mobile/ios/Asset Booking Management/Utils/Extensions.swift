//
//  Extensions.swift
//  Asset Booking Management
//
//  Created by Jakov  Petric on 18.06.2026..
//

import Foundation

extension String {
    func formattedDate(
        from input: String = "yyyy-MM-dd'T'HH:mm:ssZ",
        to output: String = "dd/MM/yyyy"
    ) -> String {
        let formatter = DateFormatter()
        formatter.dateFormat = input

        guard let date = formatter.date(from: self) else {
            return self
        }

        formatter.dateFormat = output
        return formatter.string(from: date)
    }
}
