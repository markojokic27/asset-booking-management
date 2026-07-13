//
//  Assets.swift
//  Asset Booking Management
//
//  Created by Jakov  Petric on 18.06.2026..
//

import Foundation

let mockAssetData: [AssetResponse] = [
    AssetResponse(
        id: 1,
        name: "Dell Latitude 7420",
        categoryId: 1,
        description: "Business laptop assigned to staff",
        code: "LAP-001",
        status: .ACTIVE,
        location: "Head Office"
    ),
    AssetResponse(
        id: 2,
        name: "MacBook Pro 14",
        categoryId: 1,
        description: "Development workstation",
        code: "LAP-002",
        status: .ACTIVE,
        location: "IT Department"
    ),
    AssetResponse(
        id: 3,
        name: "HP EliteBook 840",
        categoryId: 1,
        description: "Sales team laptop",
        code: "LAP-003",
        status: .ACTIVE,
        location: "Sales Office"
    ),
    AssetResponse(
        id: 4,
        name: "iPhone 15",
        categoryId: 2,
        description: "Company mobile device",
        code: "MOB-001",
        status: .ACTIVE,
        location: "Remote"
    ),
    AssetResponse(
        id: 5,
        name: "Samsung Galaxy S24",
        categoryId: 2,
        description: "Testing device",
        code: "MOB-002",
        status: .ACTIVE,
        location: "QA Lab"
    ),
    AssetResponse(
        id: 6,
        name: "Lenovo ThinkPad X1",
        categoryId: 1,
        description: "Executive laptop",
        code: "LAP-004",
        status: .ACTIVE,
        location: "Executive Office"
    ),
    AssetResponse(
        id: 7,
        name: "Cisco Switch 9300",
        categoryId: 3,
        description: "Core network switch",
        code: "NET-001",
        status: .ACTIVE,
        location: "Server Room"
    ),
    AssetResponse(
        id: 8,
        name: "Dell PowerEdge R750",
        categoryId: 4,
        description: "Application server",
        code: "SRV-001",
        status: .ACTIVE,
        location: "Data Center"
    ),
    AssetResponse(
        id: 9,
        name: "Epson EcoTank ET-4850",
        categoryId: 5,
        description: "Office printer",
        code: "PRN-001",
        status: .ACTIVE,
        location: "Reception"
    ),
    AssetResponse(
        id: 10,
        name: "iPad Air",
        categoryId: 2,
        description: "Field inspection tablet",
        code: "TAB-001",
        status: .ACTIVE,
        location: "Operations"
    ),
    AssetResponse(
        id: 11,
        name: "Surface Laptop 6",
        categoryId: 1,
        description: "Design team laptop",
        code: "LAP-005",
        status: .ACTIVE,
        location: "Creative Studio"
    ),
    AssetResponse(
        id: 12,
        name: "Brother HL-L3270CDW",
        categoryId: 5,
        description: "Color laser printer",
        code: "PRN-002",
        status: .DAMAGED,
        location: "Finance"
    ),
    AssetResponse(
        id: 13,
        name: "Cisco Meraki MX95",
        categoryId: 3,
        description: "Security appliance",
        code: "NET-002",
        status: .ACTIVE,
        location: "Server Room"
    ),
    AssetResponse(
        id: 14,
        name: "Dell OptiPlex 7010",
        categoryId: 6,
        description: "Front desk workstation",
        code: "DESK-001",
        status: .ACTIVE,
        location: "Reception"
    ),
    AssetResponse(
        id: 15,
        name: "Apple Studio Display",
        categoryId: 7,
        description: "Designer monitor",
        code: "MON-001",
        status: .ACTIVE,
        location: "Creative Studio"
    ),
    AssetResponse(
        id: 16,
        name: "LG UltraFine 32UN880",
        categoryId: 7,
        description: "Engineering monitor",
        code: "MON-002",
        status: .ACTIVE,
        location: "Engineering"
    ),
    AssetResponse(
        id: 17,
        name: "Poly Voyager Focus 2",
        categoryId: 8,
        description: "Wireless headset",
        code: "AUD-001",
        status: .ACTIVE,
        location: "Support Team"
    ),
    AssetResponse(
        id: 18,
        name: "Logitech MX Keys",
        categoryId: 8,
        description: "Wireless keyboard",
        code: "ACC-001",
        status: .ACTIVE,
        location: "Engineering"
    ),
    AssetResponse(
        id: 19,
        name: "Logitech MX Master 3S",
        categoryId: 8,
        description: "Wireless mouse",
        code: "ACC-002",
        status: .ACTIVE,
        location: "Engineering"
    ),
    AssetResponse(
        id: 20,
        name: "Dell PowerEdge T550",
        categoryId: 4,
        description: "Backup server",
        code: "SRV-002",
        status: .INACTIVE,
        location: "Disaster Recovery Site"
    )
]

