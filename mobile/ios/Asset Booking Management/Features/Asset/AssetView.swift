//
//  AssetView.swift
//  Asset Booking Management
//
//  Created by Jakov  Petric on 17.06.2026..
//

import CodeScanner
import Foundation
import SwiftUI
import AVFoundation

struct AssetView: View {
    @Environment(AssetRepository.self) private var assetRepository
    @Environment(AssetCategoryRepository.self) private var assetCategoryRepository
    @Environment(ErrorView.self) private var errorView

    @State private var searchText = ""
    @State private var showingFilters = false
    @State private var selectedCategoryId: Int64?
    @State private var selectedStatus: AssetStatus?
    @State private var showingScanner = false
    @State private var selectedTab = 0
    
    @Binding var path: NavigationPath
    
    private let tabs = ["Assets"]
    
    private var categories: [AssetCategoryResponse] {
        guard assetCategoryRepository.loadState == .loading else {
            return []
        }

        return assetCategoryRepository.assetCategories.values
                .sorted { $0.name < $1.name }
    }

    private var filteredAssets: [AssetResponse] {
        var assets = Array(assetRepository.assets.values)

        if let selectedCategoryId {
            assets = assets.filter {
                $0.categoryId == selectedCategoryId
            }
        }

        if let selectedStatus {
            assets = assets.filter {
                $0.status == selectedStatus
            }
        }

        if !searchText.isEmpty {
            let query = searchText.localizedLowercase

            assets = assets.filter {
                $0.name.localizedLowercase.contains(query) ||
                $0.code.localizedLowercase.contains(query) ||
                $0.description.localizedLowercase.contains(query) ||
                $0.location.localizedLowercase.contains(query)
            }
        }

        return assets.sorted {
            $0.name.localizedCompare($1.name) == .orderedAscending
        }
    }
    
    func handleScan(result: Result<ScanResult, ScanError>) {
        showingScanner = false

        switch result {
        case .success(let result):
            guard let id = Int64(result.string) else {
                errorView.show(AssetScannerError.invalidData)
                return
            }

            guard assetRepository.assets[id] != nil else {
                errorView.show(AssetScannerError.notFound)
                return
            }

            path.append(id)

        case .failure(let error):
            errorView.show(error)
        }
    }
    
    @ViewBuilder
    private var assetTabs: some View {
        Picker("Assets", selection: $selectedTab) {}
        .pickerStyle(.segmented)
        .padding(.horizontal)
        .frame(width: 0, height: 0)
    }

    var body: some View {
        ZStack(alignment: .bottomTrailing) {
            ScreenContainer {
                assetTabs
                
                if assetRepository.loadState == .loading {
                    ProgressView()
                } else {
                    ScrollView {
                        LazyVStack(spacing: 12) {
                            if filteredAssets.isEmpty {
                                Text("No assets found")
                                    .foregroundStyle(.secondary)
                                    .padding()
                            } else {
                                ForEach(filteredAssets) { asset in
                                    AssetCard(assetId: asset.id)
                                }
                            }
                        }
                    }
                    .scrollIndicators(.hidden)
                }
                
                Spacer(minLength: 0)
            }
            .navigationTitle(TabItem.asset.title)
            .searchable(text: $searchText, prompt: "Search assets")
            .toolbar {
                ToolbarItem(placement: .topBarTrailing) {
                    Button {
                        showingFilters = true
                    } label: {
                        Image(systemName: "line.3.horizontal.decrease.circle")
                    }
                }
            }
            .sheet(isPresented: $showingFilters) {
                NavigationStack {
                    Form {
                        Section("Category") {
                            Picker("Category", selection: $selectedCategoryId) {
                                Text("All")
                                    .tag(nil as Int64?)
                                
                                ForEach(categories) { category in
                                    Text(category.name)
                                        .tag(Optional(category.id))
                                }
                            }
                        }
                        
                        Section("Status") {
                            Picker("Status", selection: $selectedStatus) {
                                Text("All")
                                    .tag(nil as AssetStatus?)
                                
                                ForEach(AssetStatus.allCases) { status in
                                    Text(status.rawValue.capitalized)
                                        .tag(Optional(status))
                                }
                            }
                        }
                    }
                    .navigationTitle("Filters")
                    .toolbar {
                        ToolbarItem(placement: .topBarLeading) {
                            Button("Clear") {
                                selectedCategoryId = nil
                                selectedStatus = nil
                            }
                        }
                        
                        ToolbarItem(placement: .topBarTrailing) {
                            Button("Done") {
                                showingFilters = false
                            }
                        }
                    }
                }
            }
            .sheet(isPresented: $showingScanner) {
                CodeScannerView(codeTypes: [.qr], simulatedData: "1", completion: handleScan)
            }
            
            Button {
                showingScanner = true
            } label: {
                Image(systemName: "qrcode.viewfinder")
                    .font(.title2)
                    .foregroundStyle(.white)
                    .padding()
                    .background(Color.accentColor)
                    .clipShape(Circle())
                    .shadow(radius: 4)
            }
            .padding()
        }
        .refreshable {
            await assetRepository.loadAssets()
        }
    }

}
