//
//  LoginView.swift
//  Asset Booking Management
//
//  Created by Jakov  Petric on 17.06.2026..
//

import SwiftUI

struct LoginView: View {
    @Environment(AuthRepository.self) private var authManager
    @Environment(ToastManager.self) private var toastManager

    @State private var username = ""
    @State private var password = ""
    @State private var passwordVisible = false

    var body: some View {
        ScreenContainer {
            VStack(spacing: 24) {
                
                VStack(spacing: 8) {
                    Image("Logo")
                        .resizable()
                        .scaledToFit()
                        .frame(height: 80)
                    
                    Text("Asset Booking Management")
                        .font(.headline)
                }
                .padding(.top, 50)
                
                VStack(alignment: .leading, spacing: 16) {
                    
                    VStack(alignment: .leading, spacing: 6) {
                        TextField("Enter username", text: $username)
                            .padding(.horizontal, 12)
                            .frame(height: 44)
                            .background(Color(.systemGray6))
                            .clipShape(RoundedRectangle(cornerRadius: 8))
                    }
                    
                    VStack(alignment: .leading, spacing: 6) {
                        HStack {
                            Group {
                                if passwordVisible {
                                    TextField("Enter password", text: $password)
                                } else {
                                    SecureField("Enter password", text: $password)
                                }
                            }
                            .frame(height: 20)

                            Button {
                                passwordVisible.toggle()
                            } label: {
                                Image(systemName: passwordVisible ? "eye.slash" : "eye")
                            }
                        }
                        .padding(.horizontal, 12)
                        .frame(height: 44)
                        .background(Color(.systemGray6))
                        .clipShape(RoundedRectangle(cornerRadius: 8))
                    }
                    
                    AppButton(
                        isLoading: authManager.loadState == .loading,
                        variant: .primary,
                        action: {
                            Task {
                                let success = await authManager.login(
                                    username: username,
                                    password: password
                                )
                                
                                if success {
                                    toastManager.show("Login success", style: .success)
                                }
                            }
                        }
                    ) {
                        Text("Login")
                            .fontWeight(.bold)
                    }.frame(maxWidth: .infinity)
                }
                .padding(20)
                .clipShape(RoundedRectangle(cornerRadius: 12))
                .shadow(radius: 6)
                .padding(.horizontal, 10)
            }
        }
    }
}

#Preview {
    LoginView()
}
