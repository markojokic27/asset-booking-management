//
//  ProfileView.swift
//  Asset Booking Management
//
//  Created by Jakov  Petric on 17.06.2026..
//

import Foundation
import SwiftUI

struct ProfileView: View {
    @Environment(UserRepository.self) private var userRepository

    var body: some View {
        ScreenContainer {
            ScrollView {
                if let user = userRepository.loggedInUser {
                    ProfileCard(user: user)
                    WorkDetailsCard(user: user)
                } else {
                    ProgressView()
                }
            }
            .scrollIndicators(.hidden)
        }
        .navigationTitle(TabItem.profile.title)
        .buttonStyle(.plain)
    }
}

#Preview{
    ProfileView()
}
