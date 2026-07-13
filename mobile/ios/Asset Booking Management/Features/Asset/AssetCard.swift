//
//  AssetCard.swift
//  Asset Booking Management
//
//  Created by Jakov  Petric on 18.06.2026..
//

import SwiftUI

struct AssetCard: View {
    let assetId: Int64

    @Environment(AssetRepository.self) private var assetRepository

    private var asset: AssetResponse? {
        assetRepository.asset(id: assetId)
    }

    var body: some View {
        if let asset {
            NavigationLink(
                destination: AssetDetailView(
                    assetId: assetId
                )) {
                CardView {
                    HStack(alignment: .center) {
                        VStack(alignment: .leading, spacing: 4) {
                            Text(asset.name)
                                .font(.body)
                                .fontWeight(.bold)
                                .foregroundStyle(.primary)
                            
                            Text(asset.code)
                                .font(.subheadline)
                                .foregroundStyle(.primary)
                            
                            Text(asset.location)
                                .font(.caption)
                                .foregroundStyle(.primary)
                        }
                        
                        Spacer()
                        
                        StatusBadge(status: asset.status.rawValue)
                    }
                    .padding(16)
                    .frame(maxWidth: .infinity, alignment: .leading)
                }
            }
        }
    }
}
