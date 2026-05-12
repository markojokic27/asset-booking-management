# Asset Booking Management System



Welcome to the **Asset Booking Management System** — an internal platform for managing shared company resources efficiently and transparently.

***

## What Is This System?

The Asset Booking Management System enables employees, managers, and administrators to coordinate the use of shared physical resources — such as meeting rooms, laptops, parking spaces, desks, and books — through a centralized, role-based web application.

It eliminates reliance on spreadsheets, email chains, and informal agreements by providing a single source of truth for asset availability, booking state, and usage history.

***

## Core Capabilities

| Capability | Description |
|---|---|
| **Asset Management** | Create, update, deactivate assets and manage categories with booking rules |
| **Booking Lifecycle** | Create, modify, cancel, and approve bookings with conflict prevention |
| **QR Quick-Booking** | Scan an asset's QR code to instantly initiate a booking |
| **Approval Workflows** | Route bookings to managers for restricted asset categories |
| **Notifications** | Email confirmations and calendar synchronization for confirmed bookings |
| **Reporting** | Usage statistics and historical reports per asset or category |

***

## Architecture at a Glance

The system is implemented as a **modular monolith** — one Spring Boot application divided into clean domain modules (`user`, `asset-category`, `asset`, `booking`, `report`, `core`), backed by PostgreSQL and served via a React 19 frontend.

For the full architecture description, see the [Project Description page](project_des.html).

***

## Build & Quality

This site is generated as part of the Maven CI/CD pipeline and includes live reports for test results, code coverage, dependency health, and API documentation. All reports are accessible from the navigation menu.