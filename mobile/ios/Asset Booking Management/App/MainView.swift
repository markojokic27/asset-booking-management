//
//  MainView.swift
//  Asset Booking Management
//
//  Created by Jakov  Petric on 17.06.2026..
//

import Foundation
import SwiftUI

struct MainView: View {
    @State private var tabSelection = TabItem.home
    @State private var path = NavigationPath()
    
    var body: some View {
        TabView(selection: $tabSelection) {
            NavigationStack {
                HomeView(tabSelection: $tabSelection)
            }.tabItem {Label(TabItem.home.title, systemImage: TabItem.home.icon)}.tag(TabItem.home)
            NavigationStack(path: $path) {
                AssetView(path: $path)
                    .navigationDestination(for: Int64.self) { assetId in
                        AssetDetailView(assetId: assetId)
                    }
            }.tabItem {Label(TabItem.asset.title, systemImage: TabItem.asset.icon)}.tag(TabItem.asset)
            NavigationStack{
                BookingView()
            }.tabItem {Label(TabItem.booking.title, systemImage: TabItem.booking.icon)}.tag(TabItem.booking)
            NavigationStack{
                ProfileView()
            }.tabItem {Label(TabItem.profile.title, systemImage: TabItem.profile.icon)}.tag(TabItem.profile)
        }
    }
}

#Preview {
    MainView()
}
