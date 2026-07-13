//
//  WorkDetailsCard.swift
//  Asset Booking Management
//
//  Created by Jakov  Petric on 18.06.2026..
//

import SwiftUI

struct WorkDetailsCard: View {
    @Environment(DepartmentRepository.self) private var departmentRepository
    
    let user: UserResponse

    @State private var showingNotes = false
    
    private var departmentName: String {
        departmentRepository.department(id: user.departmentId)?.name.rawValue ?? "Unknown department"
    }

    var body: some View {
        CardView {
            VStack(alignment: .leading, spacing: 0) {
                VStack(alignment: .leading, spacing: 4) {
                    Text("Work details")
                        .font(.title)
                        .foregroundStyle(.primary)
                }

                BadgeInfoRow(label: "Role") {
                    StatusBadge(status: user.role.rawValue)
                }

                Divider()

                BadgeInfoRow(label: "Status") {
                    StatusBadge(status: user.status.rawValue)
                }

                Divider()

                InfoRow(
                    label: "Department",
                    value: departmentName
                )

                Divider()

                InfoRow(
                    label: "Manager",
                    value: user.managerEmail ?? "No manager email"
                )

                Divider()

                HStack {
                    Text("Notes")

                    Spacer()

                    Button("View") {
                        showingNotes = true
                    }
                    .buttonStyle(.borderedProminent)
                    .disabled(user.notes.isEmpty)
                }
                .padding(.vertical, 12)
            }
            .frame(maxWidth: .infinity, alignment: .leading)
            .padding(.horizontal, 16)
            .padding(.vertical, 18)
        }
        .padding()
        .sheet(isPresented: $showingNotes) {
            NavigationStack {
                ScrollView {
                    Text(user.notes.isEmpty ? "No notes available." : user.notes)
                        .frame(maxWidth: .infinity, alignment: .leading)
                        .padding()
                }
                .navigationTitle("Notes")
                .toolbar {
                    ToolbarItem(placement: .topBarTrailing) {
                        Button("Done") {
                            showingNotes = false
                        }
                    }
                }
            }
        }
    }
}

#Preview {
    WorkDetailsCard(user: UserResponse.preview)
}
