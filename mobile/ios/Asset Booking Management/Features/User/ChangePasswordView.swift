//
//  ChangePasswordView.swift
//  Asset Booking Management
//
//  Created by Jakov  Petric on 06.07.2026..
//

import SwiftUI

struct ChangePasswordView: View {
    @Environment(\.dismiss) private var dismiss
    @Environment(UserRepository.self) private var userRepository
    @Environment(ToastManager.self) private var toastManager

    @State private var currentPassword = ""
    @State private var newPassword = ""
    @State private var confirmPassword = ""

    @State private var currentPasswordError: String?
    @State private var newPasswordError: String?
    @State private var confirmPasswordError: String?
    @State private var requestError: String?

    @State private var isSaving = false
    
    private var user: UserResponse? {
        userRepository.loggedInUser
    }

    var body: some View {
        ScreenContainer {
            VStack(spacing: 20) {
                PasswordField(
                    title: "Current Password",
                    text: $currentPassword,
                    error: currentPasswordError,
                    enabled: !isSaving
                )

                PasswordField(
                    title: "New Password",
                    text: $newPassword,
                    error: newPasswordError,
                    enabled: !isSaving
                )

                PasswordField(
                    title: "Confirm Password",
                    text: $confirmPassword,
                    error: confirmPasswordError,
                    enabled: !isSaving
                )

                if let requestError {
                    Text(requestError)
                        .foregroundStyle(.red)
                        .font(.footnote)
                        .frame(maxWidth: .infinity, alignment: .leading)
                }

                Spacer()

                Button {
                    Task {
                        await submit()
                    }
                } label: {
                    if isSaving {
                        ProgressView()
                            .frame(maxWidth: .infinity)
                    } else {
                        Text("Save")
                            .fontWeight(.semibold)
                            .frame(maxWidth: .infinity)
                    }
                }
                .buttonStyle(.borderedProminent)
                .controlSize(.large)
                .disabled(isSaving)
            }
            .padding()
        }
        .navigationTitle("Change password")
    }

    private func submit() async {
        guard !isSaving else { return }
        guard let user else { return }

        currentPasswordError = nil
        newPasswordError = nil
        confirmPasswordError = nil
        requestError = nil

        var hasError = false

        if currentPassword.isEmpty {
            currentPasswordError = "Current password is required."
            hasError = true
        }

        if newPassword.isEmpty {
            newPasswordError = "New password is required."
            hasError = true
        }

        if confirmPassword.isEmpty {
            confirmPasswordError = "Please confirm your password."
            hasError = true
        }

        if !newPassword.isEmpty,
           !confirmPassword.isEmpty,
           newPassword != confirmPassword {
            confirmPasswordError = "Passwords do not match."
            hasError = true
        }

        if hasError {
            return
        }

        isSaving = true
        defer { isSaving = false }

        let success = await userRepository.changePassword(id: user.id, username: user.username, currentPassword: currentPassword, newPassword: newPassword)
        
        if success {
            toastManager.show("Password changed successfully", style: .success)
        }
        
        dismiss()
    }
}

private struct PasswordField: View {
    let title: String
    @Binding var text: String
    let error: String?
    let enabled: Bool

    var body: some View {
        VStack(alignment: .leading, spacing: 8) {

            Text(title)
                .font(.subheadline.weight(.medium))

            SecureField("", text: $text)
                .textFieldStyle(.plain)
                .padding(.horizontal, 14)
                .frame(height: 48)
                .background(Color(.systemBackground))
                .overlay {
                    RoundedRectangle(cornerRadius: 10)
                        .stroke(Color.gray.opacity(0.2), lineWidth: 1)
                }
                .disabled(!enabled)

            if let error {
                Text(error)
                    .font(.footnote)
                    .foregroundStyle(.red)
            }
        }
    }
}

#Preview {
    ChangePasswordView()
}
