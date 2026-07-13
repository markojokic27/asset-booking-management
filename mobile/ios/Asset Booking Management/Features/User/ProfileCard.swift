//
//  ProfileCard.swift
//  Asset Booking Management
//
//  Created by Jakov  Petric on 18.06.2026..
//

import SwiftUI

struct ProfileCard: View {
    @Environment(AuthRepository.self) private var authRepository
    @Environment(ToastManager.self) private var toastManager
    
    let user: UserResponse
    
    var body: some View {
        CardView {
            VStack(alignment: .leading, spacing: 0) {
                VStack(alignment: .leading, spacing: 4) {
                    Text("Profile details")
                        .font(.title)
                        .foregroundStyle(.primary)
                }
                
                InfoRow(label: "ID", value: String(user.id))
                Divider()
                
                InfoRow(label: "First name", value: user.name)
                Divider()
                
                InfoRow(label: "Last name", value: user.surname)
                Divider()
                
                InfoRow(label: "Username", value: user.username)
                Divider()
                
                InfoRow(label: "Email", value: user.email)
                Divider()
                
                HStack {
                    Text("Password")
                        .foregroundStyle(.primary)

                    Spacer()

                    NavigationLink {
                        ChangePasswordView()
                    } label: {
                        Text("Change Password")
                    }
                    .buttonStyle(.borderedProminent)
                }
                .padding(.vertical, 12)
                
                
                Divider()
                
                HStack {
                    Button(role: .destructive) {
                        Task {
                            let success = await authRepository.logout()
                            
                            if success {
                                toastManager.show("Logout successful", style: .success)
                            }
                        }
                    } label: {
                        Label("Logout", systemImage: "rectangle.portrait.and.arrow.right")
                    }
                    .foregroundStyle(.red)
                }
                .frame(maxWidth: .infinity)
                .padding(.vertical, 12)
            }
            .frame(maxWidth: .infinity, alignment: .leading)
            .padding(.horizontal, 16)
            .padding(.vertical, 18)
        }
        .padding()
    }
}

#Preview {
    ProfileCard(user: UserResponse.preview)
}
