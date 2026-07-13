//
//  AssetDetailView.swift
//  Asset Booking Management
//
//  Created by Jakov  Petric on 08.07.2026..
//

import SwiftUI

struct AssetDetailView: View {
    @Environment(AssetRepository.self) private var assetRepository
    @Environment(AssetCategoryRepository.self) private var assetCategoryRepository
    
    let assetId: Int64

    private var asset: AssetResponse? {
        assetRepository.asset(id: assetId)
    }
    
    private var assetCategoryName: String {
        guard let asset else { return "Unknown category" }
        
        return assetCategoryRepository.assetCategory(id: asset.categoryId)?.name ?? "Unknown category"
    }
    
    private var canBookAsset: Bool {
        guard let asset else { return false }
        
        return asset.status == .ACTIVE
    }

    var body: some View {
        ScreenContainer {
            if let asset {
                CardView {
                    VStack(alignment: .leading, spacing: 0) {

                        VStack(alignment: .leading, spacing: 4) {
                            Text(asset.name)
                                .font(.title2)
                                .fontWeight(.semibold)
                        }
                        .padding(.bottom, 16)
                        
                        InfoRow(
                            label: "Category",
                            value: assetCategoryName
                        )

                        Divider()

                        HStack {
                            Text("Status")

                            Spacer()

                            StatusBadge(
                                status: asset.status.rawValue
                            )
                        }
                        .padding(.vertical, 12)

                        Divider()

                        InfoRow(
                            label: "Location",
                            value: asset.location.isEmpty
                                ? "Unavailable"
                                : asset.location
                        )

                        Divider()

                        InfoRow(
                            label: "Description",
                            value: asset.description.isEmpty
                                ? "Unavailable"
                                : asset.description
                        )

                        Divider()
                        
                        HStack {
                            Spacer()

                            NavigationLink {
                                CreateBookingView(
                                    assetId: assetId
                                )
                            } label: {
                                Label(
                                    "Book",
                                    systemImage: "calendar"
                                )
                                .frame(minWidth: 120)
                            }
                            .buttonStyle(.borderedProminent)
                            .disabled(!canBookAsset)

                            Spacer()
                        }
                        .padding(.top, 16)
                        
                    }
                    .padding()
                }

            } else {
                ProgressView()
            }

            Spacer()
        }
        .navigationTitle("Asset Details")
    }
}
