//
//  ScreenContainer.swift
//  Asset Booking Management
//
//  Created by Jakov  Petric on 18.06.2026..
//

import Foundation
import SwiftUI

struct ScreenContainer<Content: View>: View {
    let content: Content

    init(@ViewBuilder content: () -> Content) {
        self.content = content()
    }

    var body: some View {
        VStack(alignment: .leading, spacing: 16) {
            content
        }
        .buttonStyle(.plain)
        .padding(.horizontal, 24)
        .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .top)
    }
}
