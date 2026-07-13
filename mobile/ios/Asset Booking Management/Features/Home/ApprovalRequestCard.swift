//
//  ApprovalRequestCard.swift
//  Asset Booking Management
//
//  Created by Jakov  Petric on 18.06.2026..
//

import Foundation
import SwiftUI

struct ApprovalRequestsCard: View {
    let pendingCount: Int

    var body: some View {
        HStack {
            VStack(alignment: .leading, spacing: 4) {
                Text("Pending Approvals")
                    .font(.headline)
                
                Text("\(pendingCount) requests")
                    .font(.caption)
                    .foregroundStyle(.primary)
            }
            
            Spacer()
            
            Image(systemName: "arrow.right")
        }
        .padding(.horizontal, 24)
        .padding(.vertical, 10)
        .background(Color(.systemGray6))
        .clipShape(RoundedRectangle(cornerRadius: 8))
    }
}

#Preview {
    ApprovalRequestsCard(pendingCount: 15)
}
